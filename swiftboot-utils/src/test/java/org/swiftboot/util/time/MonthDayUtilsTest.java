package org.swiftboot.util.time;

import org.apache.commons.lang3.Range;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.MonthDay;

public class MonthDayUtilsTest {

    @Test
    public void testRange() {
        Range<LocalDate> r1 = Range.of(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 31));
        // 和r1不重叠
        Range<LocalDate> r2 = Range.of(LocalDate.of(2020, 2, 1), LocalDate.of(2020, 2, 28));
        // 和r1重叠
        Range<LocalDate> r3 = Range.of(LocalDate.of(2020, 1, 15), LocalDate.of(2020, 2, 15));
        Assertions.assertTrue(r1.isBefore(LocalDate.of(2020, 2, 1)));
        Assertions.assertFalse(r1.isBefore(LocalDate.of(2020, 1, 29)));
        Assertions.assertTrue(r1.isBeforeRange(r2));
        Assertions.assertFalse(r1.isBeforeRange(r3));
    }

    @Test
    public void isOverlapping() {
        MonthDay md11 = MonthDay.of(1, 1);
        MonthDay md21 = MonthDay.of(2, 1);
        MonthDay md31 = MonthDay.of(3, 1);
        MonthDay md41 = MonthDay.of(4, 1);
        MonthDay md51 = MonthDay.of(5, 1);
        MonthDay md61 = MonthDay.of(6, 1);
        MonthDay md71 = MonthDay.of(7, 1);
        MonthDay md81 = MonthDay.of(8, 1);
        MonthDay md91 = MonthDay.of(9, 1);
        MonthDay md101 = MonthDay.of(10, 1);
        MonthDay md111 = MonthDay.of(11, 1);
        MonthDay md121 = MonthDay.of(12, 1);

        Assertions.assertTrue(MonthDayUtils.isOverlapping(md11, md31, md21, md31));
        Assertions.assertTrue(MonthDayUtils.isOverlapping(md91, md61, md21, md31));
        Assertions.assertTrue(MonthDayUtils.isOverlapping(md91, md61, md21, md101));
        Assertions.assertTrue(MonthDayUtils.isOverlapping(md91, md61, md121, md11));

        Assertions.assertFalse(MonthDayUtils.isOverlapping(md11, md21, md31, md41));
        Assertions.assertFalse(MonthDayUtils.isOverlapping(md91, md61, md71, md81));
    }

    @Test
    public void isBetweenForLocalDate() {
        // normal
        MonthDay monthDayFrom = MonthDay.from(LocalDate.of(2020, 1, 1));
        MonthDay monthDayTo = MonthDay.from(LocalDate.of(2020, 2, 2));
        Assertions.assertTrue(MonthDayUtils.isBetween(LocalDate.of(2020, 1, 1), monthDayFrom, monthDayTo));
        Assertions.assertTrue(MonthDayUtils.isBetween(LocalDate.of(2020, 1, 15), monthDayFrom, monthDayTo));
        Assertions.assertTrue(MonthDayUtils.isBetween(LocalDate.of(2020, 2, 2), monthDayFrom, monthDayTo));

        Assertions.assertFalse(MonthDayUtils.isBetween(LocalDate.of(2019, 12, 31), monthDayFrom, monthDayTo));
        Assertions.assertFalse(MonthDayUtils.isBetween(LocalDate.of(2020, 2, 3), monthDayFrom, monthDayTo));

        // over year
        monthDayFrom = MonthDay.from(LocalDate.of(2019, 12, 1)); // the year doesn't matter.
        monthDayTo = MonthDay.from(LocalDate.of(2020, 2, 2));
        Assertions.assertTrue(MonthDayUtils.isBetween(LocalDate.of(2019, 12, 1), monthDayFrom, monthDayTo));
        Assertions.assertTrue(MonthDayUtils.isBetween(LocalDate.of(2019, 12, 2), monthDayFrom, monthDayTo));
        Assertions.assertTrue(MonthDayUtils.isBetween(LocalDate.of(2020, 1, 1), monthDayFrom, monthDayTo));

        Assertions.assertFalse(MonthDayUtils.isBetween(LocalDate.of(2019, 11, 30), monthDayFrom, monthDayTo));
        Assertions.assertFalse(MonthDayUtils.isBetween(LocalDate.of(2020, 2, 3), monthDayFrom, monthDayTo));

    }

    @Test
    public void isBetweenForLocalDateTime() {
        // normal
        MonthDay monthDayFrom = MonthDay.from(LocalDateTime.of(2020, 1, 1, 12, 30, 30));
        MonthDay monthDayTo = MonthDay.from(LocalDateTime.of(2020, 2, 2, 13, 30, 30));
        Assertions.assertTrue(MonthDayUtils.isBetween(LocalDateTime.of(2020, 1, 1, 13, 30, 30), monthDayFrom, monthDayTo));
        Assertions.assertTrue(MonthDayUtils.isBetween(LocalDateTime.of(2020, 1, 15, 13, 30, 30), monthDayFrom, monthDayTo));
        Assertions.assertTrue(MonthDayUtils.isBetween(LocalDateTime.of(2020, 2, 2, 13, 30, 30), monthDayFrom, monthDayTo));

        Assertions.assertFalse(MonthDayUtils.isBetween(LocalDateTime.of(2019, 12, 31, 13, 30, 30), monthDayFrom, monthDayTo));
        Assertions.assertFalse(MonthDayUtils.isBetween(LocalDateTime.of(2020, 2, 3, 13, 30, 30), monthDayFrom, monthDayTo));

        // over year
        monthDayFrom = MonthDay.from(LocalDate.of(2019, 12, 1)); // the year doesn't matter.
        monthDayTo = MonthDay.from(LocalDate.of(2020, 2, 2));
        // 包含
        Assertions.assertTrue(MonthDayUtils.isBetween(LocalDateTime.of(2019, 12, 1, 13, 30, 30), monthDayFrom, monthDayTo));
        Assertions.assertTrue(MonthDayUtils.isBetween(LocalDateTime.of(2019, 12, 2, 13, 30, 30), monthDayFrom, monthDayTo));
        Assertions.assertTrue(MonthDayUtils.isBetween(LocalDateTime.of(2020, 1, 1, 13, 30, 30), monthDayFrom, monthDayTo));
        // 不包含
        Assertions.assertFalse(MonthDayUtils.isBetween(LocalDateTime.of(2019, 11, 30, 13, 30, 30), monthDayFrom, monthDayTo));
        Assertions.assertFalse(MonthDayUtils.isBetween(LocalDateTime.of(2020, 2, 3, 13, 30, 30), monthDayFrom, monthDayTo));

    }

}
