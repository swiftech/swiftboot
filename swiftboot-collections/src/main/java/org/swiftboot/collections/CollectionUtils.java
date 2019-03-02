package org.swiftboot.collections;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * Created by swiftech on 16/8/15.
 */
public class CollectionUtils {

    /**
     * 将key-value字符串转换成Map，key不能有重复
     *
     * @param content 格式: k1:v1,k2:v2,k3:v3
     * @return
     */
    public static Map<String, String> parseKeyValueString(String content) {
        Map<String, String> ret = new HashMap<>();
        String[] all = StringUtils.split(content, ",");
        for (String entry : all) {
            String[] kv = StringUtils.split(entry.trim(), ":");
            if (ArrayUtils.isEmpty(kv) || kv.length < 2) {
                continue;
            }
            ret.put(kv[0].trim(), kv[1].trim());
        }
        return ret;
    }

    /**
     * 数组转换为 LinkedList 对象
     *
     * @param array
     * @return
     */
    public static LinkedList<Object> toLinkedList(Object[] array) {
        LinkedList<Object> ret = new LinkedList<>();
        Collections.addAll(ret, array);
        return ret;
    }

    /**
     * 集合中是否包含指定类型
     * @param collection
     * @param clazz
     * @return
     */
    public static boolean contains(Collection collection, Class clazz) {
        if (collection == null || clazz == null) {
            return false;
        }
        for (Object o : collection) {
            if (o.getClass().equals(clazz)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取数组中第一个类型匹配的元素
     * @param collection
     * @param clazz
     * @return
     */
    public static Object getFirstMatch(Object[] collection, Class clazz) {
        if (collection == null || clazz == null) {
            return null;
        }
        for (Object o : collection) {
            if (clazz.isAssignableFrom(o.getClass())) {
                return o;
            }
        }
        return null;
    }

    /**
     * 获取数组中第一个类型匹配的元素
     * @param collection
     * @param clazz
     * @return
     */
    public static Object getFirstMatch(List collection, Class clazz) {
        if (collection == null || clazz == null) {
            return null;
        }
        for (Object o : collection) {
            if (clazz.isAssignableFrom(o.getClass())) {
                return o;
            }
        }
        return null;
    }

}
