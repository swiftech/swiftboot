package org.swiftboot.util;

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

}
