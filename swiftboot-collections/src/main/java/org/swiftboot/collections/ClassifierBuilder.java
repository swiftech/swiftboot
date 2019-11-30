package org.swiftboot.collections;

import java.util.Comparator;

/**
 * @param <T>
 * @see Classifier
 */
public class ClassifierBuilder<T extends Comparable<T>, E> {
    private Classifier.Trait<T, E> trait;
    private Classifier.Filter<E> filter;
    private Classifier.SubCollection<E> subCollectionCreator;
    private Comparator<T> traitComparator;
    private Comparator<E> collectionComparator;

    public ClassifierBuilder<T, E> setTrait(Classifier.Trait<T, E> trait) {
        this.trait = trait;
        return this;
    }

    public ClassifierBuilder<T, E> setFilter(Classifier.Filter<E> filter) {
        this.filter = filter;
        return this;
    }

    public ClassifierBuilder<T, E> setSubCollectionCreator(Classifier.SubCollection<E> subCollectionCreator) {
        this.subCollectionCreator = subCollectionCreator;
        return this;
    }

    public ClassifierBuilder<T, E> setTraitComparator(Comparator<T> traitComparator) {
        this.traitComparator = traitComparator;
        return this;
    }

    public ClassifierBuilder<T, E> setCollectionComparator(Comparator<E> collectionComparator) {
        this.collectionComparator = collectionComparator;
        return this;
    }

    public Classifier<T, E> createClassifier() {
        return new Classifier<T, E>(trait, filter, subCollectionCreator, traitComparator, collectionComparator);
    }
}