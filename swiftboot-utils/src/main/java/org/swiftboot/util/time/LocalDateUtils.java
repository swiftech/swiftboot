package org.swiftboot.util.time;

import java.time.*;
import java.util.Locale;

/**
 *
 * @since 3.2
 */
public class LocalDateUtils {

    /**
     * Get start time of {@link LocalDate} in UTC timezone.
     *
     * @param localDate
     * @param userLocale the user locale for the {@code localDate}
     * @return
     */
    public static LocalDateTime toUtcStartOfDay(LocalDate localDate, Locale userLocale) {
        if (localDate == null) return null;
        ZoneId userZoneId = TimeZoneMapper.resolveZoneId(userLocale);
        return localDate.atStartOfDay().
                atZone(userZoneId)
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime();
    }

    /**
     * Get start time of {@link LocalDate} in UTC timezone.
     *
     * @param localDate
     * @param userZoneId the user timezone for the {@code localDate}
     * @return
     */
    public static LocalDateTime toUtcStartOfDay(LocalDate localDate, ZoneId userZoneId) {
        if (localDate == null) return null;
        return localDate.atStartOfDay().
                atZone(userZoneId)
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime();
    }

    /**
     * Get end time of {@link LocalDate} in UTC timezone.
     *
     * @param localDate
     * @param userLocale the user locale for the {@code localDate}
     * @return
     */
    public static LocalDateTime toUtcEndOfDay(LocalDate localDate, Locale userLocale) {
        if (localDate == null) return null;
        ZoneId userZoneId = TimeZoneMapper.resolveZoneId(userLocale);
        return localDate.atTime(LocalTime.MAX).
                atZone(userZoneId)
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime();
    }

    /**
     * Get end time of {@link LocalDate} in UTC timezone.
     *
     * @param localDate
     * @param userZoneId the user timezone for the {@code localDate}
     * @return
     */
    public static LocalDateTime toUtcEndOfDay(LocalDate localDate, ZoneId userZoneId) {
        if (localDate == null) return null;
        return localDate.atTime(LocalTime.MAX).
                atZone(userZoneId)
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime();
    }

    /**
     * Get the number of day in a week with timezone.
     *
     * @param localDate
     * @param userZoneId the user timezone for the {@code localDate}
     * @return
     */
    public static Integer weekNumOf(LocalDate localDate, ZoneId userZoneId) {
        LocalDate today = localDate.atStartOfDay().atZone(userZoneId).toLocalDate();
        return today.getDayOfWeek().getValue();
    }

    /**
     * Get the number of day in a week with timezone.
     *
     * @param localDate
     * @param userLocale the user locale for the {@code localDate}
     * @return
     */
    public static Integer weekNumOf(LocalDate localDate, Locale userLocale) {
        if (userLocale == null) return null;
        ZoneId userZoneId = TimeZoneMapper.resolveZoneId(userLocale);
        return weekNumOf(localDate, userZoneId);
    }

    /**
     * Get the number of day in a week with timezone.
     *
     * @param userZoneId the user timezone for the {@code localDate}
     * @return
     */
    public static Integer weekNumOfToday(ZoneId userZoneId) {
        LocalDate today = LocalDate.now(userZoneId);
        return today.getDayOfWeek().getValue();
    }

    /**
     * Get the number of day in a week with timezone.
     *
     * @param userLocale the user locale for the {@code localDate}
     * @return
     */
    public static Integer weekNumOfToday(Locale userLocale) {
        if (userLocale == null) return null;
        ZoneId userZoneId = TimeZoneMapper.resolveZoneId(userLocale);
        return weekNumOfToday(userZoneId);
    }

}
