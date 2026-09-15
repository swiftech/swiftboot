package org.swiftboot.web.format;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @since 3.2
 */
public class BigDecimalPlainSerializer extends JsonSerializer<BigDecimal> {
    public static final BigDecimalPlainSerializer instance = new BigDecimalPlainSerializer();

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            if (value.compareTo(BigDecimal.ZERO) == 0) {
                gen.writeString("0");
                return;
            }
            gen.writeString(value.stripTrailingZeros().toPlainString());
        }
        else {
            gen.writeNull();
        }
    }
}
