package org.swiftboot.util.time;

import java.time.LocalDateTime;
import java.util.TimeZone;

/**
 * @since 3.0
 */
public class LocalDateTimeUtils {

    public static Long toMillisecond(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        TimeZone tz = TimeZone.getDefault();
        return localDateTime.atZone(tz.toZoneId()).toInstant().toEpochMilli();
    }


}
