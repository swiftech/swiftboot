package org.swiftboot.web.format;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.swiftboot.web.util.TimezoneUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * @since 3.2
 */
public class DynamicTimeZoneLocalDateTimeDeserializer extends BaseJsonDeserializer<LocalDateTime> {

    public DynamicTimeZoneLocalDateTimeDeserializer(DateTimeFormatter formatter) {
        super(formatter);
    }

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateStr = p.getText();
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }

        ZoneId sourceZone = TimezoneUtils.zoneIdFromCurrentLocale();

        // 2. 将前端传来的本地时间字符串解析为 LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, formatter);

        // 3. 将该时区的时间转化为 UTC LocalDateTime 存入后端
        return localDateTime.atZone(sourceZone)             // 绑定前端时区
                .withZoneSameInstant(ZoneOffset.UTC) // 转换为 UTC 时区
                .toLocalDateTime();             // 剥离时区信息，拿到 UTC 的 LocalDateTime
    }
}