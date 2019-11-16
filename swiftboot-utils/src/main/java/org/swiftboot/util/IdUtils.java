package org.swiftboot.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.UUID;

/**
 * 生成各种 ID 的工具类
 *
 * @author swiftech
 **/
public class IdUtils {

    /**
     * 生成不包含'-'分隔符的 UUID，长度固定为32字节
     *
     * @return
     */
    public static String makeUUID() {
        String uuid = UUID.randomUUID().toString();
        return StringUtils.replaceChars(uuid, "-", "");
    }

    /**
     * 通过业务码生成全局唯一 ID，ID组成如下
     * 业务对象代码，2-8字节，例如 "abba"
     * 时间戳，17字节，例如 "20190405130745000"
     * 随机数，7-13字节，例如 "oarqpotcftg"
     *
     * @param bizCode 2-8字节，例如"abba"，如果不在这个范围则抛出异常
     * @return
     */
    public static String makeID(String bizCode) {
        if (StringUtils.isBlank(bizCode)
                || bizCode.length() < 2 || bizCode.length() > 8) {
//            throw new RuntimeException(String.format("生成ID失败，参数错误: %s", bizCode));
            throw new RuntimeException(Info.get(IdUtils.class, "id_failed1", bizCode));
        }
        StringBuilder buf = new StringBuilder(32);
        buf.append(bizCode, 0, bizCode.length())
                .append(DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmssSSS"))
                .append(RandomStringUtils.randomAlphabetic(15 - bizCode.length()).toLowerCase());
        return buf.toString();
    }

    /**
     * 通过业务代码和服务器ID生成UUID：
     * 业务对象代码，2-6字节，例如 "abba"
     * 服务器ID，1-4字节，例如 "02"
     * 时间戳，17字节，例如 "20190405130745000"
     * 随机数，5-12字节 例如 "iveptlo"
     *
     * @param bizCode  2-6字节，例如"abba"，如果不在这个范围则抛出异常
     * @param serverId 1-4字节，例如"02"，如果不在这个范围则抛出异常
     * @return
     */
    public static String makeID(String bizCode, String serverId) {
        if (StringUtils.isAnyBlank(bizCode, serverId)
                || bizCode.length() < 2 || bizCode.length() > 6
                || serverId.length() < 1 || serverId.length() > 4) {
//            throw new RuntimeException(String.format("生成ID失败，参数错误: %s %s", bizCode, serverId));
            throw new RuntimeException(Info.get(IdUtils.class,"id_failed2", bizCode, serverId));
        }
        StringBuilder buf = new StringBuilder(32);
        buf.append(bizCode, 0, bizCode.length())
                .append(serverId, 0, serverId.length())
                .append(DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmssSSS"))
                .append(RandomStringUtils.randomAlphabetic(15 - bizCode.length() - serverId.length()).toLowerCase());
        return buf.toString();
    }

    /**
     * 以当前时刻生成流水号，长度固定为17字节
     *
     * @return
     */
    public static String makeSn() {
        return DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmssSSS");
    }

    /**
     * 已当前时刻+三位随机数生成流水号，长度固定为20字节
     *
     * @return
     */
    public static String makeSnRandom() {
        return makeSn() + RandomStringUtils.randomNumeric(3);
    }
}
