package org.swiftboot.util.time;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;

class YearUtilsTest {

    @Test
    public void toUtcStartOfYear() {
        LocalDateTime utcStartOfYear = YearUtils.toUtcStartOfYear(Year.now(), ZoneId.of("Asia/Shanghai"));
        System.out.println("start of year: " + utcStartOfYear);
    }

    @Test
    public void toUtcEndOfYear() {
        LocalDateTime utcEndOfYear = YearUtils.toUtcEndOfYear(Year.now(), ZoneId.of("Asia/Shanghai"));
        System.out.println("end of year: " +utcEndOfYear);
    }

    public static void main(String[] args) {
        System.out.println(Year.now());
        System.out.println(Year.now().atDay(1));
        System.out.println(Year.now().atDay(1).atStartOfDay(ZoneId.of("Asia/Shanghai")));
    }

}