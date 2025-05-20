package org.swiftboot.util.time;

import org.apache.commons.lang3.Range;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.util.List;

/**
 * @since 3.0.0
 */
public class MonthDayUtils {

    /**
     * 判断两个 {@link MonthDay} 范围是否重叠。
     *
     * @param from1
     * @param to1
     * @param from2
     * @param to2
     * @return
     */
    public static boolean isOverlapping(MonthDay from1, MonthDay to1, MonthDay from2, MonthDay to2) {
        List<Range<MonthDay>> ranges1 = toRanges(from1, to1);
        List<Range<MonthDay>> ranges2 = toRanges(from2, to2);
        for (Range<MonthDay> r1 : ranges1) {
            for (Range<MonthDay> r2 : ranges2) {
                if (r1.isOverlappedBy(r2)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 转成成 {@link Range}，如果跨年，则拆分成两个返回
     *
     * @param from
     * @param to
     * @return
     */
    public static List<Range<MonthDay>> toRanges(MonthDay from, MonthDay to) {
        if (from.getMonthValue() > to.getMonthValue()) {
            return List.of(Range.of(from, MonthDay.of(12, 31)), Range.of(MonthDay.of(1, 1), to));
        }
        else if (from.getMonthValue() == to.getMonthValue()) {
            if (from.getDayOfMonth() > to.getDayOfMonth()) {
                return List.of(Range.of(from, MonthDay.of(12, 31)), Range.of(MonthDay.of(1, 1), to));
            }
            else {
                return List.of(Range.of(from, to));
            }
        }
        else {
            return List.of(Range.of(from, to));
        }
    }

    /**
     * 日期是否在 from 和 to 之间，忽略时间。
     *
     * @param dateTime
     * @param start
     * @param end
     * @return
     */
    public static boolean isBetween(LocalDateTime dateTime, MonthDay start, MonthDay end) {
        // 将日期调整为当前年份的MonthDay
        MonthDay md = MonthDay.from(dateTime);

        if (start.isBefore(end)) {
            // 不跨年情况
            return !md.isBefore(start) && !md.isAfter(end);
        }
        else {
            // 跨年情况: 日期在start之后或end之前
            return !md.isBefore(start) || !md.isAfter(end);
        }
    }

    /**
     * 日期 date 是否在 from 和 to 之间
     *
     * @param date
     * @param start inclusive
     * @param end inclusive
     * @return
     */
    public static boolean isBetween(LocalDate date, MonthDay start, MonthDay end) {
        // 将日期调整为当前年份的MonthDay
        MonthDay md = MonthDay.from(date);

        if (start.isBefore(end)) {
            // 不跨年情况
            return !md.isBefore(start) && !md.isAfter(end);
        }
        else {
            // 跨年情况: 日期在start之后或end之前
            return !md.isBefore(start) || !md.isAfter(end);
        }
    }

}
