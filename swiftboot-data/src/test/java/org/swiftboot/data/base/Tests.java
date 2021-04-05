package org.swiftboot.data.base;

/**
 * Do test in a transaction with input and returns.
 *
 * @author swiftech
 */
@FunctionalInterface
public interface Tests<T, R> {
    R onTest(T param);
}
