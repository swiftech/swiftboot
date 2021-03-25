package org.swiftboot.data.base;

/**
 * Assert the result in new single transaction.
 *
 * @author swiftech
 */
@FunctionalInterface
public interface AssertsWithResult<T, R> {
    R onAssert(T param);
}
