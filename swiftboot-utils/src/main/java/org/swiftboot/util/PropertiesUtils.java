package org.swiftboot.util;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author swiftech
 * @since 1.1
 */
public class PropertiesUtils {

    /**
     *
     * @param entries
     * @param handler
     * @param <T> 表示文件的类，例如 File，ZipEntry 等等
     * @return
     */
    public static <T> List<T> extractResourceFilesByLocale(List<T> entries, ExtractHandler<T> handler) {
        Locale curLocale = Locale.getDefault();
        System.out.println("Current locale: " + curLocale);
        List<Locale> locales = LocaleUtils.availableLocaleList(); // TODO only SIMPLIFIED_CHINESE for now
        locales = new LinkedList<Locale>() {
            {
                add(Locale.US);
                add(Locale.SIMPLIFIED_CHINESE);
            }
        };

        // find non-locale resource files;
        List<T> nonLocaleFiles = new LinkedList<>();
        for (T entry : entries) {
            long countOfLocaled = locales.stream().filter(
                    locale -> handler.getFileName(entry).endsWith(locale.toString() + ".properties"))
                    .count();
            if (countOfLocaled <= 0) {
                nonLocaleFiles.add(entry);
            }
        }
        System.out.println("nonLocaleFiles: " + nonLocaleFiles.size());

        // find files to load by current locale
        List<T> ret = new LinkedList<>();
        for (T nonLocaleEntry : nonLocaleFiles) {
            String nonLocaleFileName = StringUtils.substringBeforeLast(handler.getFileName(nonLocaleEntry), ".properties");
            handler.onPureFileName(nonLocaleFileName);
            List<T> collect = entries.stream().filter(
                    file -> handler.getFileName(file).equals(
                            String.format("%s_%s.properties", nonLocaleFileName, curLocale)))
                    .collect(Collectors.toList());
            if (!collect.isEmpty()) {
                ret.addAll(collect);
            }
            else {
                ret.add(nonLocaleEntry);
            }
        }
        return  ret;
    }

    public interface ExtractHandler<T> {
        /**
         * 获取文件名称
         *
         * @param t
         * @return
         */
        String getFileName(T t);

        /**
         * 得到纯名称（不带locale）
         */
        void onPureFileName(String pureName);
    }


}
