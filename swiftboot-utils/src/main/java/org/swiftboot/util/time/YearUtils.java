package org.swiftboot.util.time;

import java.time.*;

/**
 * @since 3.2
 */
public class YearUtils {

    /**
     * Get start time of {@link Year} in UTC timezone.
     *
     * @param year
     * @param zoneId
     * @return
     */
    public static LocalDateTime toUtcStartOfYear(Year year, ZoneId zoneId) {
        return LocalDateTimeUtils.toUtc(
                year.atDay(1).atStartOfDay(zoneId)
        );
    }

    /**
     * Get start time of {@link Year} in UTC timezone.
     *
     * @param year
     * @param zoneId
     * @return
     */
    public static LocalDateTime toUtcEndOfYear(Year year, ZoneId zoneId) {
        return LocalDateTimeUtils.toUtc(
                year.atMonth(Month.DECEMBER).atEndOfMonth().atTime(LocalTime.MAX), zoneId
        );
    }
}
