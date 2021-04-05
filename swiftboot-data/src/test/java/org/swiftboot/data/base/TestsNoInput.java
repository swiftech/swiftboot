package org.swiftboot.data.base;

/**
 * Do test in a transaction without any input and returns.
 *
 * @author swiftech
 */
@FunctionalInterface
public interface TestsNoInput<R> {

    R onTest();
}
