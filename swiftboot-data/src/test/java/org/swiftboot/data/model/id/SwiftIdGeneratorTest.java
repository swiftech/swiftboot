package org.swiftboot.data.model.id;

import org.junit.jupiter.api.Test;
import org.swiftboot.data.model.id.SwiftIdGenerator.BusinessId;

public class SwiftIdGeneratorTest {

    @Test
    public void next() {
        // 假设业务代码: 1001 (订单模块), 服务器节点: 12
        long businessCode = 1001;
        long nodeId = 12;

        SwiftIdGenerator generator = new SwiftIdGenerator( nodeId);

        // 生成 ID
        BusinessId id = generator.nextId(businessCode);

        System.out.println("=== 生成的 ID 各种表现形式 ===");
        System.out.println("32位 Hex 字符串 (API/VARCHAR): " + id.toHexString());
        System.out.println("字节数组长度 (BINARY(16)):    " + id.toBytes().length + " 字节");
        System.out.println("UUID 格式:                    " + id.toUUID());

        // 解析最高位以验证业务代码
        long extractedBusinessCode = (id.getMostSigBits() >>> 48) & 0xFFFF;
        System.out.println("验证提取的业务代码:             " + extractedBusinessCode);

        //
        String newId = generator.generate("hello every one");
        System.out.println("newId: " + newId);
    }
}
