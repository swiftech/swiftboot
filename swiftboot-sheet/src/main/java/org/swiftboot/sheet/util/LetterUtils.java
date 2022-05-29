package org.swiftboot.sheet.util;

/**
 * @author swiftech
 */
public class LetterUtils {


    /**
     * Convert a letter (ignore case) to index in [A-Z].
     *
     * @param letter
     * @return
     */
    public static int letterToIndex(char letter) {
        return Character.toUpperCase(letter) - 'A';
    }

    /**
     * Convert number in alphabet to a letter (upper case)
     *
     * @param num
     * @return
     */
    public static char numberToLetter(int num) {
        return (char) ('A' + num - 1);
    }
}
