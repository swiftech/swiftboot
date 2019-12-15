package org.swiftboot.collections;

import java.util.*;

/**
 * 集合分类器。
 * 可以把任何集合按照元素特征进行分类，形成分类标识和子集合的映射。
 * 分类过程中可以过滤调不需要的元素
 * 分类后的结果集合可以进行排序：
 * 如果设置了比较器，则强制返回有序的子集合；
 * 否则，按照原集合中的顺序填充子集合
 *
 * @param <T> 特征的类型
 * @param <E> 元素类型
 * @author swiftech
 * @see ClassifierBuilder
 * @since 1.1
 */
public final class Classifier<T extends Comparable<T>, E> {

    private Trait<T, E> trait;
    private Filter<E> filter;
    private SubCollection<E> subCollectionCreator;
    private Comparator<T> traitComparator;
    private Comparator<E> collectionComparator;
    private Converter<E, ?> converter; // TODO

    public Classifier(Trait<T, E> trait,
                      Filter<E> filter,
                      SubCollection<E> subCollectionCreator,
                      Comparator<T> traitComparator,
                      Comparator<E> collectionComparator) {
        this.trait = trait;
        this.filter = filter;
        this.subCollectionCreator = subCollectionCreator;
        this.traitComparator = traitComparator;
        this.collectionComparator = collectionComparator;
    }

    private Collection<E> createSubCollection(Collection<E> originalCollection) {
        if (collectionComparator != null) {
            if (originalCollection instanceof List) {
                return new LinkedList<>();
            }
            else if (originalCollection instanceof Set) {
                return new TreeSet<>(collectionComparator);
            }
            else {
                throw new RuntimeException("Not supported collection");
            }
        }
        else {
            if (this.subCollectionCreator != null) {
                return this.subCollectionCreator.create();
            }
            else {
                if (originalCollection instanceof ArrayList) {
                    return new ArrayList<>();
                }
                else if (originalCollection instanceof LinkedList) {
                    return new LinkedList<>();
                }
                else if (originalCollection instanceof HashSet) {
                    return new HashSet<>();
                }
                else if (originalCollection instanceof TreeSet) {
                    return new TreeSet<>(((TreeSet<E>) originalCollection).comparator());
                }
                else {
                    return new LinkedList<>();
                }
            }
        }
    }

    /**
     * 开始进行分类
     *
     * @param collection
     * @return
     */
    public Map<T, Collection<E>> classify(Collection<E> collection) {
        Map<T, Collection<E>> ret;
        if (this.traitComparator != null) {
            ret = new TreeMap<>(this.traitComparator);
        }
        else {
            ret = new HashMap<>();
        }
        // 过滤以及分类
        for (E e : collection) {
            if (this.filter != null && !this.filter.filter(e)) {
                continue;
            }
            T key = this.trait.trait(e);
            Collection<E> coll = ret.get(key);
            if (coll == null) {
                coll = this.createSubCollection(collection);
                ret.put(key, coll);
            }
            coll.add(e);
        }
        // 开始排序
        for (T trait : ret.keySet()) {
            Collection<E> subCollection = ret.get(trait);
            if (subCollection instanceof TreeSet) {
                continue;// TreeSet 已经排序了
            }
            if (collectionComparator != null) {
                Collection<E> sorted = CollectionUtils.sortCollection(subCollection, collectionComparator);
                ret.put(trait, sorted);
            }
        }
        return ret;
    }

    /**
     * 分类特征
     *
     * @param <T>
     * @param <E>
     */
    public interface Trait<T, E> {
        T trait(E e);
    }

    /**
     * 过滤器
     *
     * @param <E>
     */
    public interface Filter<E> {
        /**
         * 返回 true 则保留，返回 false 则过滤掉
         *
         * @param e
         * @return
         */
        boolean filter(E e);
    }

    /**
     * 创建子集合
     *
     * @param <E>
     */
    public interface SubCollection<E> {
        Collection<E> create();
    }

    public interface Converter<S, T> {
        T convert(S s);
    }
}
