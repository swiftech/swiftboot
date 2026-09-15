package org.swiftboot.util.time;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class LocalDateTimeUtilsTest {

    @Test
    void localToUtc() {
        LocalDateTime utc = LocalDateTimeUtils.toUtc(LocalDateTime.now(), ZoneId.of("GMT+8"));
        System.out.println(utc);
    }

    @Test
    void zonedToUtc() {
        LocalDateTime utc = LocalDateTimeUtils.toUtc(LocalDateTime.now().atZone(ZoneId.of("GMT+8")));
        System.out.println(utc);
    }
}