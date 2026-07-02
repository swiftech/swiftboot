package org.swiftboot.util.time;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CronUtilsTest {


    @Test
    void testToCronExpressionMDHMS() {
        String cronExpression = CronUtils.toCronExpression(7, 1, 21, 35, 40);
        Assertions.assertEquals("40 35 21 1 7 ?", cronExpression);
    }

    @Test
    void testToCronExpressionDHMS() {
        String cronExpression = CronUtils.toCronExpression(1, 21, 35, 40);
        Assertions.assertEquals("40 35 21 1 * ?", cronExpression);
    }

    @Test
    void testToCronExpressionHMS() {
        String cronExpression = CronUtils.toCronExpression(21, 35, 40);
        Assertions.assertEquals("40 35 21 * * ?", cronExpression);
    }

    @Test
    void testToCronExpressionMS() {
        String cronExpression = CronUtils.toCronExpression(35, 40);
        Assertions.assertEquals("40 35 * * * ?", cronExpression);
    }

    @Test
    void testToCronExpressionS() {
        String cronExpression = CronUtils.toCronExpression(40);
        Assertions.assertEquals("40 * * * * ?", cronExpression);
    }
}