package org.swiftboot.web.format;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.swiftboot.web.util.TimezoneUtils;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @since 3.2
 */
public class DynamicTimeZoneInstantSerializer extends BaseJsonSerializer<Instant> {

    public DynamicTimeZoneInstantSerializer(DateTimeFormatter formatter) {
        super(formatter);
    }

    @Override
    public void serialize(Instant instant, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        ZoneId zoneId = TimezoneUtils.zoneIdFromCurrentLocale();
        String dateStr = instant.atZone(zoneId).format(formatter);
        jsonGenerator.writeString(dateStr);
    }
}
