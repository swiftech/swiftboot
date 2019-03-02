package org.swiftboot.collections;

import java.util.*;

/**
 * @author swiftech 2016-09-29
 **/
public class MapUtils {


    /**
     * 按照 ClassifyFilter 接口返回的 key 值分类存放元素集合（和原集合相同类型的集合）
     *
     * @param collection
     * @param classifyFilter
     * @return
     */
    public static Map classify(Collection collection, ClassifyFilter classifyFilter) {
        Map<String, Collection> ret = new HashMap<>();
        for (Object o : collection) {
            String key = classifyFilter.filter(o);
            Collection coll = ret.get(key);
            if (coll == null) {
                if (collection instanceof List) {
                    coll = new ArrayList();
                }
                else if (collection instanceof Set) {
                    coll = new HashSet();
                }
                else {

                }
                ret.put(key, coll);
            }
            coll.add(o);
        }
        return ret;
    }

    /**
     * 分类过滤器，用于将集合分类成Map
     * @param <T>
     */
    public interface ClassifyFilter<T> {
        String filter(T t);
    }

}
