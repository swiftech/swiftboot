package org.swiftboot.sheet;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Utils for creating testing data for swiftboot-sheet.
 *
 * @author swiftech
 */
public class TestUtils {

    /**
     * B,C,D...
     *
     * @return
     */
    public static String[] createLetters(int lastIndex) {
        char[] chars = "BCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        char[] subChars = ArrayUtils.subarray(chars, 0, (lastIndex - 1) + 1); // -1 for missing 'A', +1 for exclusive end index
        String[] ret = new String[subChars.length];
        for (int i = 0; i < subChars.length; i++) {
            char aChar = subChars[i];
            ret[i] = String.valueOf(aChar);
        }
        return ret;
    }

    /**
     * 2,3,4...
     *
     * @param lastIndex
     * @return
     */
    public static String[] createNumbers(int lastIndex) {
        int count = lastIndex + 1;
        String[] ret = new String[count - 1];
        for (int i = 1; i < count; i++) {
            ret[i - 1] = String.valueOf(i + 1);
        }
        return ret;
    }

    public static void main(String[] args) {
        System.out.println(StringUtils.join(createLetters(5), ","));
        System.out.println(StringUtils.join(createNumbers(5), ","));

        System.out.println(StringUtils.join(createLetters(12), ","));
        System.out.println(StringUtils.join(createNumbers(12), ","));
    }
}
