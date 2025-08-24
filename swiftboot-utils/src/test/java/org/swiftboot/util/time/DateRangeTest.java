package org.swiftboot.util.time;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class DateRangeTest {

    @Test
    public void containsDate() {
        LocalDate date1 = LocalDate.now().minusDays(5);
        LocalDate date2 = LocalDate.now();
        LocalDate date3 = LocalDate.now().plusDays(5);
        DateRange dr13 = new DateRange(date1, date3);
        Assertions.assertTrue(dr13.contains(date1));
        Assertions.assertTrue(dr13.contains(date2));
        Assertions.assertTrue(dr13.contains(date3));

        DateRange dr23 = new DateRange(date2, date3);
        Assertions.assertFalse(dr23.contains(date1));
        Assertions.assertTrue(dr23.contains(date2));
        Assertions.assertTrue(dr23.contains(date3));
    }

    @Test
    public void containsDateTime() {
        LocalDate date1 = LocalDate.now();
        LocalDate date2 = LocalDate.now().plusDays(5);
        LocalDate date3 = LocalDate.now().plusDays(10);
        DateRange dr12 = new DateRange(date1, date2);

        Assertions.assertTrue(dr12.contains(date1.atTime(0, 0, 0).plusHours(1)));
        Assertions.assertTrue(dr12.contains(date1.atTime(0, 0, 0)));
        Assertions.assertTrue(dr12.contains(date2.plusDays(1).atTime(0, 0, 0).minusSeconds(1)));

        Assertions.assertFalse(dr12.contains(date1.atTime(0, 0, 0).minusSeconds(1)));
        Assertions.assertFalse(dr12.contains(date2.plusDays(1).atTime(0, 0, 0)));
    }

    @Test
    public void intersect() {
        LocalDate date1 = LocalDate.now();
        LocalDate date2 = LocalDate.now().plusDays(5);
        LocalDate date3 = LocalDate.now().plusDays(10);
        LocalDate date4 = LocalDate.now().plusDays(15);
        DateRange dr12 = new DateRange(date1, date2);
        DateRange dr13 = new DateRange(date1, date3);
        DateRange dr23 = new DateRange(date2, date3);
        DateRange dr24 = new DateRange(date2, date4);
        DateRange dr34 = new DateRange(date3, date4);
        Assertions.assertTrue(dr13.intersects(dr24));
        Assertions.assertTrue(dr12.intersects(dr23));
        Assertions.assertTrue(dr23.intersects(dr12));
        Assertions.assertFalse(dr12.intersects(dr34));
    }

    @Test
    public void unite() {
        LocalDate date1 = LocalDate.now();
        LocalDate date2 = LocalDate.now().plusDays(5);
        LocalDate date3 = LocalDate.now().plusDays(10);
        LocalDate date4 = LocalDate.now().plusDays(15);
        DateRange dr12 = new DateRange(date1, date2);
        DateRange dr13 = new DateRange(date1, date3);
        DateRange dr23 = new DateRange(date2, date3);
        DateRange dr24 = new DateRange(date2, date4);
        DateRange dr34 = new DateRange(date3, date4);
        Assertions.assertEquals(new DateRange(date1, date4), dr13.unite(dr24));
        Assertions.assertNull(dr12.unite(dr34));
    }
}
