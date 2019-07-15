package org.swiftboot.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author swiftech
 **/
public class NumberFormatUtilsTest {

    @Test
    public void test() {
        Assertions.assertEquals("0.36", NumberFormatUtils.formatCurrency(0.361f));
        Assertions.assertEquals("0.37", NumberFormatUtils.formatCurrency(0.367f));
        Assertions.assertEquals("1234.57", NumberFormatUtils.formatCurrency(1234.567f));
        Assertions.assertEquals("123.00", NumberFormatUtils.formatCurrency(123.00f));
        Assertions.assertEquals("99999999.99", NumberFormatUtils.formatCurrency(99999999.99));
    }

    @Test
    public void testPercent() {
        Assertions.assertEquals(Double.parseDouble("38.5"), NumberFormatUtils.toPercent(0.385f));
        Assertions.assertEquals(Double.parseDouble("12.35"), NumberFormatUtils.toPercent(0.12345f));
    }

    @Test
    public void testPercentRound() {
        Assertions.assertEquals(Double.parseDouble("38.5"), NumberFormatUtils.toPercent(0.385f, 3));
        Assertions.assertEquals(Double.parseDouble("12.345"), NumberFormatUtils.toPercent(0.12345f, 3));
    }
}
