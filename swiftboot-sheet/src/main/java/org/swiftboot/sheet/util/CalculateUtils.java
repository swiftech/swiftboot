package org.swiftboot.sheet.util;

/**
 * @author allen
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
}
