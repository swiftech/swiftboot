package org.swiftboot.util;

import java.util.List;

/**
 * @author swiftech
 * @since 2.0.2
 */
public class BooleanUtils {

    /**
     * Performs an 'or' operation on a set of booleans.
     * Null in the list will be ignored
     *
     * @param list
     * @return
     */
    public static Boolean or(List<Boolean> list) {
        Boolean ret = null;
        for (final Boolean element : list) {
            if (element != null) {
                if (element) {
                    return Boolean.TRUE;
                }
                else {
                    ret = Boolean.FALSE;
                }
            }
        }
        return ret;
    }

    /**
     * Performs an 'or' operation on a set of booleans.
     * Null in the array will be ignored
     *
     * @param array
     * @return
     */
    public static Boolean or(Boolean... array) {
        Boolean ret = null;
        for (final Boolean element : array) {
            if (element != null) {
                if (element) {
                    return Boolean.TRUE;
                }
                else {
                    ret = Boolean.FALSE;
                }
            }
        }
        return ret;
    }
}
