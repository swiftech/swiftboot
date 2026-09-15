package org.swiftboot.data.model.id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.data.model.entity.IdPersistable;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * SwiftBoot 默认的主键生成器，用来替代原来默认的 UUID 主键生成器
 * 其组成结构如下：
 *
 * +----------------+--------------------------+-----------------+-----------------+
 * |     16 bit     |          48 bit          |      16 bit     |      48 bit     |
 * |   业务对象代码   |  时间戳 (自定义Epoch毫秒)  |    服务器节点    |     随机数/序列   |
 * +----------------+--------------------------+-----------------+-----------------+
 *
 * @since 3.2
 */
public class SwiftIdGenerator implements IdGenerator<IdPersistable> {


    // 自定义起始时间点 Epoch (例如 2026-01-01T00:00:00Z 的毫秒时间戳)
    private static final long CUSTOM_EPOCH = 1767225600000L;

    // 位数限制与掩码
    private static final long MAX_BUSINESS_CODE = 0xFFFFL;      // 16 bit (0 ~ 65535)
    private static final long MAX_NODE_ID = 0xFFFFL;           // 16 bit (0 ~ 65535)
    private static final long MAX_SEQUENCE = 0xFFFFL;          // 16 bit (毫秒内计数器 0 ~ 65535)

    private static final Logger log = LoggerFactory.getLogger(SwiftIdGenerator.class);

    //    private final long businessCode;
    private final long nodeId;
    private final SecureRandom random = new SecureRandom();

    private long lastTimestamp = -1L;
    private long sequence = 0L;

    /**
     * @param nodeId 当前服务器节点 ID (0 ~ 65535)
     */
    public SwiftIdGenerator(long nodeId) {
        log.info("SwiftIdGenerator initialized");
        if (nodeId < 0 || nodeId > MAX_NODE_ID) {
            throw new IllegalArgumentException("Node ID must be between 0 and " + MAX_NODE_ID);
        }
        this.nodeId = nodeId;
    }


    @Override
    public String generate(IdPersistable object) {
        return nextId(stringTo16BitUnsignedHash(object.getClass().getSimpleName())).toHexString();
    }

    @Override
    public String generate(String bizName) {
        return nextId(stringTo16BitUnsignedHash(bizName)).toHexString();
    }

    /**
     * 如果你需要无符号的 16-bit 整数 (范围: 0 到 65535)
     */
    public static int stringTo16BitUnsignedHash(String input) {
        return stringTo16BitHash(input) & 0xFFFF;
    }

    /**
     * 将输入字符串转换为 16-bit 整数 (short)
     *
     * @param input 输入字符串
     * @return 16位整数 (范围: -32768 到 32767)
     */
    public static short stringTo16BitHash(String input) {
        if (input == null || input.isEmpty()) {
            return 0;
        }

        // 1. 转为 UTF-8 字节数组
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);

        // 2. 截断截取：不超过 32 字节
        int length = Math.min(bytes.length, 32);

        // 3. FNV-1a 算法核心 (32位常量)
        final int FNV_32_PRIME = 0x01000193;
        final int FNV_32_INIT = 0x811c9dc5;

        int hash32 = FNV_32_INIT;
        for (int i = 0; i < length; i++) {
            hash32 ^= (bytes[i] & 0xFF);
            hash32 *= FNV_32_PRIME;
        }

        // 4. 降维折叠 (XOR Fold) 到 16-bit：混合高 16 位和低 16 位，减少冲突
        int hash16 = (hash32 >>> 16) ^ (hash32 & 0xFFFF);

        return (short) hash16;
    }

    /**
     * 生成 128-bit ID (包装为自定义 BusinessId 对象)
     *
     * @param bizCode 业务对象代码 (0 ~ 65535)
     */
    public synchronized BusinessId nextId(long bizCode) {
        if (bizCode < 0 || bizCode > MAX_BUSINESS_CODE) {
            throw new IllegalArgumentException("Business code must be between 0 and %d, but is %d".formatted(MAX_BUSINESS_CODE, bizCode));
        }
        long currentTimestamp = timeGen();

        // 时钟回拨处理
        if (currentTimestamp < lastTimestamp) {
            long offset = lastTimestamp - currentTimestamp;
            if (offset <= 5) {
                try {
                    wait(offset << 1);
                    currentTimestamp = timeGen();

                    if (currentTimestamp < lastTimestamp) {
                        throw new RuntimeException(String.format("Clock moved backwards. Refusing to generate id for %d ms", offset));
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
                }
            }
            else {
                throw new RuntimeException(String.format("Clock moved backwards by %d ms. Refusing to generate id", offset));
            }
        }

        // 同一毫秒内并发自增
        if (lastTimestamp == currentTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 毫秒内自增溢出，等待下一毫秒
            if (sequence == 0) {
                currentTimestamp = tilNextMillis(lastTimestamp);
            }
        }
        else {
            // 不同毫秒重置序列号（用随机值填充低位初始值，增加防推算能力）
            sequence = random.nextInt(10);
        }

        lastTimestamp = currentTimestamp;

        long timeOffset = currentTimestamp - CUSTOM_EPOCH;

        // 构造高 64 位 (High 64 bits):
        // 16 bit 业务代码 | 48 bit 时间戳
        long mostSigBits = (bizCode << 48) | (timeOffset & 0xFFFFFFFFFFFFL);

        // 构造低 64 位 (Low 64 bits):
        // 16 bit 节点 ID | 16 bit 自增序列 | 32 bit 安全随机数
        long randomValue = random.nextInt() & 0xFFFFFFFFL;
        long leastSigBits = (nodeId << 48) | (sequence << 32) | randomValue;

        return new BusinessId(mostSigBits, leastSigBits);
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    // =========================================================================
    // ID 实体与输出格式封装
    // =========================================================================

    public static class BusinessId {
        private final long mostSigBits;
        private final long leastSigBits;

        public BusinessId(long mostSigBits, long leastSigBits) {
            this.mostSigBits = mostSigBits;
            this.leastSigBits = leastSigBits;
        }

        public long getMostSigBits() {
            return mostSigBits;
        }

        public long getLeastSigBits() {
            return leastSigBits;
        }

        /**
         * 转为 32 位 Hex 字符串 (适合 API 传输与 VARCHAR(32) 存储)
         */
        public String toHexString() {
            return String.format("%016x%016x", mostSigBits, leastSigBits);
        }

        /**
         * 转为 16 字节二进制数组 (适合 BINARY(16) / BYTEA / Blob 存储)
         */
        public byte[] toBytes() {
            ByteBuffer buffer = ByteBuffer.allocate(16);
            buffer.putLong(mostSigBits);
            buffer.putLong(leastSigBits);
            return buffer.array();
        }

        /**
         * 转为 java.util.UUID 实例
         */
        public java.util.UUID toUUID() {
            return new java.util.UUID(mostSigBits, leastSigBits);
        }

        @Override
        public String toString() {
            return toHexString();
        }
    }

    public static void main(String[] args) {
        long l = 1001L << 48;
        System.out.printf("%016x%n", l);
    }
}
