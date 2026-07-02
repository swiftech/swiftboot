package org.swiftboot.util.time;

import org.apache.commons.lang3.ObjectUtils;

import java.time.LocalDateTime;

/**
 * Utils for Cron expression in Spring.
 *
 * @since 3.0
 */
public class CronUtils {

    /**
     * Cron expression from LocalDateTime for Spring.
     *
     * @param dateTime
     * @return
     */
    public static String toCronExpression(LocalDateTime dateTime) {
        // Spring doesn't support year in the expression.
        return String.format("%d %d %d %d %d ?",
                dateTime.getSecond(),      // seconds (0-59)
                dateTime.getMinute(),      // minutes (0-59)
                dateTime.getHour(),       // hours (0-23)
                dateTime.getDayOfMonth(),  // day of month (1-31)
                dateTime.getMonthValue() // month (1-12)
        );
    }

    /**
     * Construct a CRON expression by providing the month, day, hour, minute, and second; if any variable is null, it is converted to `*`.
     *
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static String toCronExpression(Integer month, Integer day, Integer hour, Integer minute, Integer second) {
        return String.format("%s %s %s %s %s ?",
                ObjectUtils.getIfNull(second, "*"),      // seconds (0-59)
                ObjectUtils.getIfNull(minute, "*"),      // minutes (0-59)
                ObjectUtils.getIfNull(hour, "*"),       // hours (0-23)
                ObjectUtils.getIfNull(day, "*"),  // day of month (1-31)
                ObjectUtils.getIfNull(month, "*") // month (1-12)
        );
    }

    /**
     * Construct a CRON expression by providing the day, hour, minute, and second; if any variable is null, it is converted to `*`, and the month is always `*`.
     *
     * @param day
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static String toCronExpression(Integer day, Integer hour, Integer minute, Integer second) {
        return toCronExpression(null, day, hour, minute, second);
    }

    /**
     * Construct a CRON expression by providing the hour, minute, and second; if any variable is null, it is converted to `*` (note that the month and day are always `*`).
     *
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static String toCronExpression(Integer hour, Integer minute, Integer second) {
        return toCronExpression(null, hour, minute, second);
    }

    /**
     * Construct a CRON expression by providing the minute, and second; if any variable is null, it is converted to `*` (note that the month and day and hour are always `*`).
     *
     * @param minute
     * @param second
     * @return
     */
    public static String toCronExpression(Integer minute, Integer second) {
        return toCronExpression(null, minute, second);
    }

    /**
     * Construct a CRON expression by providing the second; if the variable is null, it is converted to `*` (note that the month and day and hour and minute are always `*`).
     *
     * @param second
     * @return
     */
    public static String toCronExpression(Integer second) {
        return toCronExpression(null, second);
    }
}
