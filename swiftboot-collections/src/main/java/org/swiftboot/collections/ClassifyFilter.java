package org.swiftboot.collections;

/**
 * 分类过滤器，用于将集合分类成Map
 *
 * @param <T> 集合中的元素类型
 * @deprecated Classifier
 */
public interface ClassifyFilter<T> {

    /**
     * 获取集合分类标识符
     *
     * @param t 集合元素
     * @return 集合分类标识
     */
    String filter(T t);
}
