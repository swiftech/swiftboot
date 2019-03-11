package org.swiftboot.collections;

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
        for (Object o : array) {
            if (clazz.isAssignableFrom(o.getClass())) {
                return o;
            }
        }
        return null;
    }
}
