package org.swiftboot.data.util;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.ClassUtils;
import org.springframework.util.SystemPropertyUtils;
import org.swiftboot.util.time.TimeZoneMapper;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author swiftech 2019-02-10
 **/
public class SpringUtils {

    /**
     * 把 basePackage 名称转换为类资源路径
     *
     * @param basePackage
     * @return
     */
    public static String resolveBasePackage(String basePackage) {
        // 先处理系统变量占位符，再做转换
        return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
    }

    /**
     * Get the LocalDateTime of current user.
     *
     * @return
     * @since 3.2
     */
    public static LocalDateTime userNow() {
        ZoneId userZoneId = TimeZoneMapper.resolveZoneId(LocaleContextHolder.getLocale());
        return LocalDateTime.now(userZoneId);
    }

    /**
     * Get the ZoneId of current user, if no locale for current user, use system default locale.
     *
     * @return
     * @since 3.2
     */
    public static ZoneId userZoneId() {
        return TimeZoneMapper.resolveZoneId(LocaleContextHolder.getLocale());
    }

}
