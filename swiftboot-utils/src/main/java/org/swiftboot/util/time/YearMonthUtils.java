package org.swiftboot.util.time;

import java.time.*;

/**
 * @since 3.2
 */
public class YearMonthUtils {

    /**
     * Get start time of {@link YearMonth} in UTC timezone.
     *
     * @param yearMonth
     * @param zoneId
     * @return
     */
    public static LocalDateTime toUtcStartOfMonth(YearMonth yearMonth, ZoneId zoneId) {
        return LocalDateTimeUtils.toUtc(
                yearMonth.atDay(1).atStartOfDay(zoneId)
        );
    }

    /**
     * Get end time of {@link YearMonth} in UTC timezone.
     *
     * @param yearMonth
     * @param zoneId
     * @return
     */
    public static LocalDateTime toUtcEndOfMonth(YearMonth yearMonth, ZoneId zoneId) {
        return LocalDateTimeUtils.toUtc(
                yearMonth.atEndOfMonth().atTime(LocalTime.MAX), zoneId
        );
    }
}
