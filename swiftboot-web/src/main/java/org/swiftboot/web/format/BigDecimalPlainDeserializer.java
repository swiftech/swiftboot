package org.swiftboot.web.format;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @since 3.2
 */
public class BigDecimalPlainDeserializer extends JsonDeserializer<BigDecimal> {

    public static final BigDecimalPlainDeserializer instance = new BigDecimalPlainDeserializer();

    @Override
    public BigDecimal deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String value = jsonParser.getText();

        // 1. 处理 null 或 空字符串/纯空格，统一返回 null
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            // 2. 解析为 BigDecimal
            BigDecimal decimal = new BigDecimal(value.trim());

            // 3. 值为 0 时（如 "0.0000"），统一返回标准的 BigDecimal.ZERO，防止 scale 膨胀
            if (decimal.compareTo(BigDecimal.ZERO) == 0) {
                return BigDecimal.ZERO;
            }

            // 4. 去除末尾无用的 0，规范化内存中的对象
            return decimal.stripTrailingZeros();
        } catch (NumberFormatException e) {
            // 5. 输入非法字符（如 "abc"）时抛出 Jackson 标准反序列化异常
            throw deserializationContext.weirdStringException(value, BigDecimal.class, "无法将字符串解析为有效的 BigDecimal 数字");
        }
    }
}
