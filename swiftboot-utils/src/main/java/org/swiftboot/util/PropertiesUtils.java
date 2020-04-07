package org.swiftboot.util;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author swiftech
 * @since 1.2
 */
public class PropertiesUtils {

    /**
     * 过滤出当前 Locale 对应的所有 properties 文件，如果此 Locale 对应的文件不存在，则取默认的没有 Locale 标识的文件
     *
     * @param entries
     * @param handler
     * @param <T> 表示文件的类型，例如 File，ZipEntry 等等
     * @return
     */
    public static <T> List<T> filterPropertiesByLocale(List<T> entries, ExtractHandler<T> handler) {
        List<String> localeNames = new LinkedList<>();
        for (Locale locale : LocaleUtils.availableLocaleSet()) {
            localeNames.add(locale.getLanguage() + "_" + locale.getCountry());
        }

//        Collections.sort(localeNames, (o1,o2)->o1.compareTo(o2));
//        for (String locale : localeNames) {
//            System.out.println(locale);
//        }

        // find non-locale resource files;
        List<T> nonLocaleFiles = new LinkedList<>();
        for (T entry : entries) {
            long countOfLocaled = localeNames.stream().filter(
                    locale -> handler.getFileName(entry).endsWith(locale + ".properties"))
                    .count();
            if (countOfLocaled <= 0) {
                nonLocaleFiles.add(entry);
            }
        }
//        System.out.println("nonLocaleFiles: " + nonLocaleFiles.size());

        // find files to load by current locale
        Locale curLocale = Locale.getDefault();
//        System.out.println("Current locale: " + curLocale);
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
