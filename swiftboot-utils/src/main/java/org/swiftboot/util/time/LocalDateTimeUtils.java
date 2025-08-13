package org.swiftboot.util.time;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.TimeZone;

/**
 * @since 3.0.0
 */
public class LocalDateTimeUtils {

    /**
     * Convert {@link LocalDateTime} to {@link ZonedDateTime} with default time zone.
     *
     * @param localDateTime
     * @return
     */
    public static ZonedDateTime toZonedDateTime(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.systemDefault());
    }

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
     *
     * @param date
     * @return
     */
    public static LocalDateTime fromUtilDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Comparing 2 LocalDateTime instances ignoring year (and also the millisecond)
     *
     * @param localDateTime1
     * @param localDateTime2
     * @return
     */
    public static boolean equalsIgnoreYear(LocalDateTime localDateTime1, LocalDateTime localDateTime2) {
        return localDateTime1 != null && localDateTime2 != null
                && localDateTime1.getMonth() == localDateTime2.getMonth()
                && localDateTime1.getDayOfMonth() == localDateTime2.getDayOfMonth()
                && localDateTime1.getHour() == localDateTime2.getHour()
                && localDateTime1.getMinute() == localDateTime2.getMinute()
                && localDateTime1.getSecond() == localDateTime2.getSecond();
    }


}
