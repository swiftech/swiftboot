package org.swiftboot.util.time;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

/**
 * @since 3.0.0
 */
public class LocalDateTimeUtils {

    /**
     * Convert {@link LocalDateTime} to millisecond with default time zone.
     *
     * @param localDateTime
     * @return
     */
    public static Long toMillisecond(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        TimeZone tz = TimeZone.getDefault();
        return localDateTime.atZone(tz.toZoneId()).toInstant().toEpochMilli();
    }

    /**
     * Convert a {@link LocalDateTime} to {@link Date} with default time zone.
     *
     * @param localDateTime
     * @return
     */
    public static Date toUtilDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Convert a {@link Date} to {@link LocalDateTime} with default time zone.
     * @param date
     * @return
     */
    public static LocalDateTime fromUtilDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }


}
