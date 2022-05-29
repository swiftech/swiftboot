package org.swiftboot.data.base;

/**
 * Prepare test data in new single transaction.
 *
 * @author swiftech
 */
@FunctionalInterface
public interface PreparesWithResult<T, R> {
    R onPrepare(T param);
}
