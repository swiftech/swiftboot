package org.swiftboot.data.base;

/**
 * Assert the result in new single transaction.
 *
 * @author swiftech
 * @deprecated
 */
@FunctionalInterface
public interface Asserts<T> {
    void onAssert(T param);
}
