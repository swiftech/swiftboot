package org.swiftboot.data.base;

/**
 * Do test in a transaction.
 *
 * @author swiftech
 */
@FunctionalInterface
public interface Tests<T, R> {
    R onTest(T param);
}
