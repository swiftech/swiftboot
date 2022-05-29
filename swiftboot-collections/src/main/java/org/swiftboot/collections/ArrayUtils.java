package org.swiftboot.collections;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author swiftech
 **/
public class ArrayUtils {

    /**
     * Merge two arrays
     *
     * @param a1
     * @param a2
     * @return null if arrays are null; empty array if at least one array is empty; merged array if at least one array is not empty.
     */
    public static int[] merge(int[] a1, int[] a2) {
        if (a1 == null && a2 == null) {
            return null;
        }
        if (a1 == null) {
            return a2;
        }
        else if (a2 == null) {
            return a1;
        }
        if (a1.length == 0 && a2.length == 0) {
            return new int[]{};
        }
        int[] ret = new int[a1.length + a2.length];
        System.arraycopy(a1, 0, ret, 0, a1.length);
        System.arraycopy(a2, 0, ret, a1.length, a2.length);
        return ret;
    }

    /**
     * 获取数组中第一个类型匹配的元素
     *
     * @param array
     * @param clazz
     * @return
     */
    public static Object getFirstMatch(Object[] array, Class clazz) {
        if (array == null || clazz == null) {
            return null;
        }
        Optional<Object> first = Arrays.stream(array)
                .filter(s -> clazz.isAssignableFrom(s.getClass()))
                .findFirst();
        return first.orElse(null);
    }

}
