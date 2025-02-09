package org.swiftboot.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author swiftech
 */
class CalcUtilsTest {

    @Test
    void equalsIgnoreScale() {
        Assertions.assertTrue(CalcUtils.equalsIgnoreScale(0.5004553734061931f, 0.5001, 2));
    }
}