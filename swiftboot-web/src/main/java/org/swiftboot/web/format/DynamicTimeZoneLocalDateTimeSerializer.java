package org.swiftboot.web.format;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.web.util.TimezoneUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * For serialize JSON request body
 *
 * @since 3.2
 */
public class DynamicTimeZoneLocalDateTimeSerializer extends BaseJsonSerializer<LocalDateTime> {

    private static final Logger log = LoggerFactory.getLogger(DynamicTimeZoneLocalDateTimeSerializer.class);

    public DynamicTimeZoneLocalDateTimeSerializer(DateTimeFormatter formatter) {
        super(formatter);
    }

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }
        ZoneId targetZone = TimezoneUtils.zoneIdFromCurrentLocale();
        if (log.isTraceEnabled()) log.trace("User time zone: %s".formatted(targetZone.toString()));

        // 3. 【核心步骤】将无时区的 LocalDateTime 显式指定为 UTC，再转换到目标时区
        String formattedDate = value.atOffset(ZoneOffset.UTC) // 声明它是 UTC 时间
                .atZoneSameInstant(targetZone) // 转换到目标时区（自动算时差）
                .format(super.formatter);

        gen.writeString(formattedDate);
    }
}