package org.swiftboot.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author allen
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
        return org.apache.commons.lang3.StringUtils.lastIndexOf(substr, search, startPos);
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
        return org.apache.commons.lang3.StringUtils.lastIndexOfIgnoreCase(substr, search, startPos);
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

}
