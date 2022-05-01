package org.swiftboot.collections;

import java.util.Comparator;

/**
 * Builder for {@link Classifier}
 * 
 * @param <T>
 * @param <E>
 * @see Classifier
 */
public class ClassifierBuilder<T extends Comparable<T>, E> {
    private Classifier.Trait<T, E> trait;
    private Classifier.Filter<E> filter;
    private Classifier.SubCollection<E> subCollectionCreator;
    private Comparator<T> traitComparator;
    private Comparator<E> collectionComparator;

    /**
     * 特征器，必须设置
     * @param trait
     * @return
     */
    public ClassifierBuilder<T, E> trait(Classifier.Trait<T, E> trait) {
        this.trait = trait;
        return this;
    }

    /**
     * 过滤器，可选
     * @param filter
     * @return
     */
    public ClassifierBuilder<T, E> filter(Classifier.Filter<E> filter) {
        this.filter = filter;
        return this;
    }

    /**
     *
     * @param subCollectionCreator
     * @return
     */
    public ClassifierBuilder<T, E> subCollectionCreator(Classifier.SubCollection<E> subCollectionCreator) {
        this.subCollectionCreator = subCollectionCreator;
        return this;
    }

    /**
     * 特征排序器，可选
     * @param traitComparator
     * @return
     */
    public ClassifierBuilder<T, E> traitComparator(Comparator<T> traitComparator) {
        this.traitComparator = traitComparator;
        return this;
    }

    /**
     * 子集合排序器，可选
     * @param collectionComparator
     * @return
     */
    public ClassifierBuilder<T, E> collectionComparator(Comparator<E> collectionComparator) {
        this.collectionComparator = collectionComparator;
        return this;
    }

    public Classifier<T, E> createClassifier() {
        return new Classifier<T, E>(trait, filter, subCollectionCreator, traitComparator, collectionComparator);
    }
}