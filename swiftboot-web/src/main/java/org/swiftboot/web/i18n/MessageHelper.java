package org.swiftboot.web.i18n;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Get i18n message by user client's Accept-Language header.
 *
 * @since 3.0.0
 */
@Component
public class MessageHelper {

    @Resource
    private MessageSource messageSource;

    @Resource
    private ResourcePatternResolver resourcePatternResolver;

    public String getMessage(String key) {
        Locale userLocale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, null, userLocale);
    }

    public String getMessage(String key, Object... args) {
        Locale userLocale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, args, userLocale);
    }

    public List<Locale> getSupportedLocales(String baseName) throws IOException {
        List<Locale> supportedLocales = new ArrayList<>();

        String pattern = "classpath*:%s_*.properties".formatted(baseName.replace('.', '/'));
        org.springframework.core.io.Resource[] resources = resourcePatternResolver.getResources(pattern);

        for (org.springframework.core.io.Resource resource : resources) {
            String filename = resource.getFilename();
            if (filename != null && filename.startsWith(baseName)) {
                String localePart = filename.substring(baseName.length() + 1,
                        filename.lastIndexOf('.'));
                supportedLocales.add(Locale.forLanguageTag(localePart.replace('_', '-')));
            }
        }
        return supportedLocales;
    }
}
