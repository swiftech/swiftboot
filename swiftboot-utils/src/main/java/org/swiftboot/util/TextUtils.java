package org.swiftboot.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author swiftech
 */
public class TextUtils {

    /**
     * Finds the last index within a CharSequence
     * before the specified position {@code startPos}.
     *
     * @param text
     * @param search
     * @param startPos the position search starts from (inclusive)
     * @return
     */
    public static int lastIndexOf(String text, String search, int startPos) {
        String substr = org.apache.commons.lang3.StringUtils.substring(text, 0, startPos + 1); // +1 for startPos is inclusive
        return Strings.CS.lastIndexOf(substr, search, startPos);
    }

    /**
     * Case in-sensitive find of the last index within a CharSequence
     * before the specified position {@code startPos}.
     *
     * @param text
     * @param search
     * @param startPos
     * @return
     */
    public static int lastIndexOfIgnoreCase(String text, String search, int startPos) {
        String substr = org.apache.commons.lang3.StringUtils.substring(text, 0, startPos + 1); // +1 for startPos is inclusive
        return Strings.CI.lastIndexOf(substr, search, startPos);
    }

    /**
     * Remove any quotation mark from given text.
     *
     * @param text
     */
    public static String removeQuotes(String text) {
        String pattern = "['`\"](.*)['`\"]";
        Matcher matcher = Pattern.compile(pattern).matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return text;
    }

    /**
     * Join a list of strings ignoring the null or the blank strings.
     *
     * @param list
     * @param separator
     * @return
     * @since 2.4.9
     */
    public static String join(List<String> list, String separator) {
        list.removeIf(s -> s == null || StringUtils.isBlank(s));
        return StringUtils.join(list, separator);
    }

    /**
     * Join an array of strings ignoring the null or the blank strings.
     *
     * @param array
     * @param separator
     * @return
     * @since 2.4.9
     */
    public static String join(String[] array, String separator) {
        List<String> withoutNullOrBlank = new ArrayList<>();
        for (String s : array) {
            if (StringUtils.isNotBlank(s)) {
                withoutNullOrBlank.add(s);
            }
        }
        return StringUtils.join(withoutNullOrBlank, separator);
    }

    /**
     * Split a string with separator and return non-blank substrings as {@code List}, return null if no non-blank substring exists.
     *
     * @param str
     * @param separator
     * @return
     */
    public static List<String> splitWithoutBlank(String str, String separator) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        List<String> list = Arrays.stream(StringUtils.split(str, ",")).map(String::trim).toList();
        List<String> notBlankList = list.stream().filter(StringUtils::isNotBlank).toList();
        if (notBlankList.isEmpty()) {
            return null;
        }
        return notBlankList;
    }

}
