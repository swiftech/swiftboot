package org.swiftboot.util.time;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

class LocalDateUtilsTest {

    @Test
    public void toUtcDateTime() {
        LocalDateTime utcDateTime = LocalDateUtils.toUtcStartOfDay(LocalDate.of(2026, 07, 20), ZoneId.of("GMT+8"));
        System.out.println(utcDateTime);
    }


}