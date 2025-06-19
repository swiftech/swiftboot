package org.swiftboot.util.time;

import java.time.LocalDateTime;

/**
 *
 */
public class CronUtils {

    /**
     * Cron expression from LocalDateTime for Spring.
     *
     * @param dateTime
     * @return
     */
    public static String toCronExpression(LocalDateTime dateTime) {
        return String.format("%d %d %d %d %d ?",
                dateTime.getSecond(),      // seconds (0-59)
                dateTime.getMinute(),      // minutes (0-59)
                dateTime.getHour(),       // hours (0-23)
                dateTime.getDayOfMonth(),  // day of month (1-31)
                dateTime.getMonthValue() // month (1-12)
        );
    }
}
