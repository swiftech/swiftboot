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
        Assertions.assertEquals(BigDecimal.ONE, CalcUtils.add(BigDecimal.ONE, null));
        Assertions.assertEquals(BigDecimal.ONE, CalcUtils.add(null, BigDecimal.ONE));
        Assertions.assertEquals(BigDecimal.ZERO, CalcUtils.add(null, null));
    }

    @Test
    void subtract() {
        Assertions.assertEquals(BigDecimal.ONE, CalcUtils.subtract(BigDecimal.ONE, null));
        Assertions.assertEquals(BigDecimal.valueOf(-1), CalcUtils.subtract(null, BigDecimal.ONE));
        Assertions.assertEquals(BigDecimal.ZERO, CalcUtils.subtract(null, null));
    }

    @Test
    void min() {
        Assertions.assertEquals(BigDecimal.ZERO, CalcUtils.min(BigDecimal.ONE, BigDecimal.ZERO));
        Assertions.assertEquals(BigDecimal.ZERO, CalcUtils.min(BigDecimal.ZERO, BigDecimal.ONE));
        Assertions.assertNull(CalcUtils.min(null, null));
        BigDecimal a = BigDecimal.valueOf(100);
        BigDecimal b = BigDecimal.valueOf(100);
        Assertions.assertTrue(a == CalcUtils.min(a, b));
    }

    @Test
    void max() {
        Assertions.assertEquals(BigDecimal.ONE, CalcUtils.max(BigDecimal.ONE, BigDecimal.ZERO));
        Assertions.assertEquals(BigDecimal.ONE, CalcUtils.max(BigDecimal.ZERO, BigDecimal.ONE));
        Assertions.assertNull(CalcUtils.max(null, null));
        BigDecimal a = BigDecimal.valueOf(100);
        BigDecimal b = BigDecimal.valueOf(100);
        Assertions.assertTrue(a == CalcUtils.max(a, b));
    }

    @Test
    void limitIn() {
        BigDecimal min = BigDecimal.valueOf(10);
        BigDecimal max = BigDecimal.valueOf(20);
        Assertions.assertEquals(min, CalcUtils.limitIn(BigDecimal.valueOf(5), min, max));
        Assertions.assertEquals(BigDecimal.valueOf(15), CalcUtils.limitIn(BigDecimal.valueOf(15), min, max));
        Assertions.assertEquals(max, CalcUtils.limitIn(BigDecimal.valueOf(25), min, max));
    }
}