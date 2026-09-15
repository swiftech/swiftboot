package org.swiftboot.web.util;

import org.springframework.context.i18n.LocaleContextHolder;
import org.swiftboot.util.time.TimeZoneMapper;

import java.time.ZoneId;
import java.util.Locale;

/**
 *
 * @since 3.2
 */
public class TimezoneUtils {

    /**
     * Get the timezone for current user's locale.
     *
     * @return
     */
    public static ZoneId zoneIdFromCurrentLocale() {
        // 1. from resolving `Accept-Language` header.
        Locale currentLocale = LocaleContextHolder.getLocale();
        // 2. from locale-timezone mapping（like Asia/Shanghai or America/New_York）
        return TimeZoneMapper.resolveZoneId(currentLocale);
    }
}
