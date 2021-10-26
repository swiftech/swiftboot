package org.swiftboot.collections.map;

/**
 * 键值对处理器，实现 Map 集合元素的处理
 *
 * @author swiftech
 **/
@FunctionalInterface
public interface KeyValueHandler<K, V> {

    void handle(K key, V value) throws Exception;
}
