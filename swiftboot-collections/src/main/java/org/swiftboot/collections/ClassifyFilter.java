package org.swiftboot.collections;

/**
 * 分类过滤器，用于将集合分类成Map
 * @param <T>
 */
public interface ClassifyFilter<T> {
    String filter(T t);
}
