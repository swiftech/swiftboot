package org.swiftboot.web.format;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.swiftboot.web.util.TimezoneUtils;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @since 3.2
 */
public class DynamicTimeZoneOffsetDateTimeSerializer extends BaseJsonSerializer<OffsetDateTime> {

    public DynamicTimeZoneOffsetDateTimeSerializer(DateTimeFormatter formatter) {
        super(formatter);
    }

    @Override
    public void serialize(OffsetDateTime offsetDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        ZoneId zoneId = TimezoneUtils.zoneIdFromCurrentLocale();
        String dateStr = offsetDateTime.atZoneSameInstant(zoneId).format(formatter);
        jsonGenerator.writeString(dateStr);
    }
}
