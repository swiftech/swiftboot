package org.swiftboot.sheet.util;

/**
 * @author swiftech
 */
public class CalculateUtils {

    /**
     * Multiply 26 with 26 in specified count.
     *
     * @param count
     * @return
     */
    public static int powForExcel(int count) {
        if (count == 0) {
            return 0;
        }
        int ret = 1;
        for (int i = 0; i < count; i++) {
            ret = ret * 26;
        }
        return ret;
    }

    /**
     * Return min one, ignored if null.
     *
     * @param i
     * @param j
     * @return
     */
    public static Integer min(Integer i, Integer j) {
        if (i == null) {
            if (j == null) {
                throw new RuntimeException();
            }
            return j;
        }
        else {
            if (j == null) {
                return i;
            }
            return Math.min(i, j);
        }
    }

    /**
     * Return max one, ignored if null.
     * @param i
     * @param j
     * @return
     */
    public static Integer max(Integer i, Integer j) {
        if (i == null) {
            if (j == null) {
                throw new RuntimeException();
            }
            return j;
        }
        else {
            if (j == null) {
                return i;
            }
            return Math.max(i, j);
        }
    }
}
