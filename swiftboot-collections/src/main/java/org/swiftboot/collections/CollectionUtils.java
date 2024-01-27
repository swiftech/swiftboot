package org.swiftboot.collections;

import java.util.*;

/**
 * @author swiftech
 */
public class CollectionUtils {

    /**
     * 数组转换为 LinkedList 对象
     *
     * @param array
     * @return
     */
    public static LinkedList<Object> toLinkedList(Object... array) {
        LinkedList<Object> ret = new LinkedList<>();
        Collections.addAll(ret, array);
        return ret;
    }

    /**
     * 集合中是否包含指定类型的元素
     *
     * @param collection
     * @param clazz
     * @return
     */
    public static boolean contains(Collection<?> collection, Class<?> clazz) {
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
     *
     * @param collection
     * @param clazz
     * @return
     */
    public static Object getFirstMatch(Object[] collection, Class<?> clazz) {
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
     *
     * @param collection
     * @param clazz
     * @return
     */
    public static Object getFirstMatch(List<?> collection, Class<?> clazz) {
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
     * 根据不同的集合类型构造不同的集合实例
     *
     * @param collectionType
     * @param <T>
     * @return
     */
    public static <T extends Collection<?>> T constructCollectionByType(Class<T> collectionType) {
        if (Set.class.isAssignableFrom(collectionType)) {
            return (T) new HashSet<>();
        }
        else if (List.class.isAssignableFrom(collectionType)) {
            return (T) new ArrayList<>();
        }
        else {
            return null;
        }
    }

    /**
     * 按照 comparator 定义比较器排序集合，支持 List 和 Set 类型的集合
     *
     * @param collection
     * @param comparator
     * @param <T>
     * @return
     */
    public static <T> Collection<T> sortCollection(Collection<T> collection, Comparator<T> comparator) {
        if (collection instanceof List) {
            ((List<T>) collection).sort(comparator);
            return collection;
        }
        else if (collection instanceof Set) {
            TreeSet<T> ret = new TreeSet<T>(comparator);
            ret.addAll(collection);
            return ret;
        }
        else {
            return collection;
        }
    }

    /**
     * Whether a collections contains duplicate (by equals) elements.
     *
     * @param collection
     * @return
     * @param <T>
     */
    public static <T> boolean hasDuplicate(Collection<T> collection) {
        Set<T> hashSet = new HashSet<>();
        for (T t : collection) {
            if (hashSet.contains(t)) {
                return true;
            }
            hashSet.add(t);
        }
        return false;
    }
}
