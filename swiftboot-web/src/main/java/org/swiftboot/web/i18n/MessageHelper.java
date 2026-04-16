package org.swiftboot.web.i18n;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Get i18n message by user client's Accept-Language header.
 *
 * @since 3.0.0
 */
@Component
public class MessageHelper {

    private static final Logger log = LoggerFactory.getLogger(MessageHelper.class);

    @Resource
    private MessageSource messageSource;

    @Resource
    private ResourcePatternResolver resourcePatternResolver;

    /**
     * Static utils method to retrieve message from specified message source object.
     * Be used for module only message source.
     *
     * @param messageSource
     * @param code
     * @return
     */
    public static String getMessage(MessageSource messageSource, String code){
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    /**
     * Static utils method to retrieve message from specified message source object with arguments.
     * Be used for module only message source.
     *
     * @param messageSource
     * @param code
     * @param args
     * @return
     */
    public static String getMessage(MessageSource messageSource, String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    public String getMessage(String key) {
        Locale userLocale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, null, userLocale);
    }

    public String getMessage(String key, Object... args) {
        Locale userLocale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, args, userLocale);
    }

    /**
     * Resolve supported locales from the resource files with base name.
     *
     * @param baseName like com.example.message
     * @return
     * @throws IOException
     */
    public List<Locale> getSupportedLocales(String baseName) throws IOException {
        List<Locale> supportedLocales = new ArrayList<>();

        String pattern = "classpath*:%s_*.properties".formatted(baseName.replace('.', '/'));
        log.debug("Resolve locales from resource file pattern: %s".formatted(pattern));
        org.springframework.core.io.Resource[] resources = resourcePatternResolver.getResources(pattern);

        for (org.springframework.core.io.Resource resource : resources) {
            String filename = resource.getFilename();
            if (filename != null && filename.startsWith(baseName)) {
                String localePart = filename.substring(baseName.length() + 1,
                        filename.lastIndexOf('.'));
                supportedLocales.add(Locale.forLanguageTag(localePart.replace('_', '-')));
            }
        }
        log.debug("Resolved supported languages: %s".formatted(StringUtils.join(supportedLocales, ",")));
        return supportedLocales;
    }
}
