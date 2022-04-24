package org.swiftboot.collections.tree;

import java.util.List;

/**
 * The elements in the list must have parent-child relationship.
 * The {@link Tree} instance will composite tree structure by tracing parent of all element.
 *
 * @param <T> Data object.
 * @author swiftech
 * @see Tree
 * @since 2.2
 */
public interface Initiator<T> {

    /**
     * Provide a default root node only if user not provide explicit root node.
     *
     * @return
     */
    T defaultRoot();

    /**
     * Find parent of data object 'current' in data list 'allElement'.
     * If no parent found, just return null.
     *
     * @param allElement
     * @param current
     * @return
     */
    T findParent(List<T> allElement, T current);
}
