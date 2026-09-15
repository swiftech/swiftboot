package org.swiftboot.web.format;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.swiftboot.web.util.TimezoneUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @since 3.2
 */
public class DynamicTimeZoneOffsetDateTimeDeserializer extends BaseJsonDeserializer<OffsetDateTime> {

    public DynamicTimeZoneOffsetDateTimeDeserializer(DateTimeFormatter formatter) {
        super(formatter);
    }

    @Override
    public OffsetDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String text = jsonParser.getText().trim();
        if (text.isBlank()) {
            return null;
        }
        ZoneId zoneId = TimezoneUtils.zoneIdFromCurrentLocale();
        return LocalDateTime.parse(text, super.formatter)
                .atZone(zoneId).toOffsetDateTime();
    }
}
