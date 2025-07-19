package org.swiftboot.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

/**
 * @author swiftech
 */
class CalcUtilsTest {

    @Test
    void equalsIgnoreScale() {
        Assertions.assertTrue(CalcUtils.equalsIgnoreScale(0.5004553734061931f, 0.5001, 2));
    }

    @Test
    void add() {
        Assertions.assertEquals(BigDecimal.valueOf(1), CalcUtils.add(BigDecimal.valueOf(1), null));
        Assertions.assertEquals(BigDecimal.valueOf(1), CalcUtils.add(null, BigDecimal.valueOf(1)));
        Assertions.assertEquals(BigDecimal.ZERO, CalcUtils.add(null, null));
    }

    @Test
    void subtract() {
        Assertions.assertEquals(BigDecimal.valueOf(1), CalcUtils.subtract(BigDecimal.valueOf(1), null));
        Assertions.assertEquals(BigDecimal.valueOf(-1), CalcUtils.subtract(null, BigDecimal.valueOf(1)));
        Assertions.assertEquals(BigDecimal.ZERO, CalcUtils.subtract(null, null));
    }
}