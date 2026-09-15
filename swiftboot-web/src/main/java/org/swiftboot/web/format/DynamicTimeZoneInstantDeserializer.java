package org.swiftboot.web.format;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.swiftboot.web.util.TimezoneUtils;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @since 3.2
 */
public class DynamicTimeZoneInstantDeserializer extends BaseJsonDeserializer<Instant> {

    public DynamicTimeZoneInstantDeserializer(DateTimeFormatter formatter) {
        super(formatter);
    }

    @Override
    public Instant deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String text = jsonParser.getText().trim();
        if (text.isBlank()) {
            return null;
        }
        ZoneId zoneId = TimezoneUtils.zoneIdFromCurrentLocale();
        return LocalDateTime.parse(text, super.formatter)
                .atZone(zoneId).toInstant();
    }
}
