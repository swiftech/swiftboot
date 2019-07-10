package org.swiftboot.collections;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author swiftech
 **/
public class ArrayUtils {

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
