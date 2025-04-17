package org.swiftboot.data.base;

/**
 * Prepare test data in new single transaction.
 *
 * @author swiftech
 * @deprecated
 */
@FunctionalInterface
public interface Prepares<R> {
    R onPrepare();
}
