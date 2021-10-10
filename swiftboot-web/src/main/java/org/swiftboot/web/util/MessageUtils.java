package org.swiftboot.web.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

/**
 * @author swiftech
 * @since 2.2
 */
public class MessageUtils {


    /**
     * Instantiate a parametrized message with providing arguments.
     *
     * @param message like "xxx{0}xxx{1}xxx"
     * @param args
     * @return
     */
    public static String instantiateMessage(String message, String[] args) {
        String[] argp = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            argp[i] = String.format("{%d}", i);
        }
        return StringUtils.replaceEach(message, argp, args);
    }

    /**
     * Concat sentences by current {@link Locale} of system.
     *
     * @param sentences
     * @return
     */
    public static String concatSentences(String... sentences) {
        Locale defaultLocale = Locale.getDefault();
        if (Locale.CHINESE.equals(defaultLocale)
                || Locale.JAPANESE.equals(defaultLocale)
                || Locale.KOREAN.equals(defaultLocale)) {
            return StringUtils.join(sentences);
        }
        else {
            return StringUtils.join(sentences, " ");
        }
    }
}
