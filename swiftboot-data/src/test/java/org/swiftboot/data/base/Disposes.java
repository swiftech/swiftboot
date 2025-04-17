package org.swiftboot.data.base;

/**
 * Dispose test data
 *
 * @author swiftech
 * @deprecated
 */
@FunctionalInterface
public interface Disposes<T> {

    /**
     * parameters from preparing stage.
     *
     * @param p
     */
    void onDispose(T p);
}
