package org.swiftboot.util.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Date range between two {@link LocalDate}s.
 *
 * @see LocalDate
 * @since 3.0.0
 */
public class DateRange {

    private LocalDate start;
    private LocalDate end;

    public DateRange(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }

    public boolean contains(LocalDate date) {
        return !start.isAfter(date) && !end.isBefore(date);
    }

    public boolean contains(LocalDateTime dateTime) {
        return !start.atTime(0, 0, 0).isAfter(dateTime)
                && end.plusDays(1).atTime(0, 0, 0).isAfter(dateTime);
    }

    public boolean intersects(DateRange dateRange) {
        return !this.start.isAfter(dateRange.end)
                && !this.end.isBefore(dateRange.start);
    }

    /**
     * Unite if the two DateRange intersects, else return null.
     *
     * @param dateRange
     * @return
     */
    public DateRange unite(DateRange dateRange) {
        if (intersects(dateRange)) {
            return new DateRange(start.isBefore(dateRange.getStart()) ? start : dateRange.getStart(), end.isAfter(dateRange.getEnd()) ? end : dateRange.getEnd());
        }
        return null;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DateRange dateRange = (DateRange) o;
        return Objects.equals(start, dateRange.start) && Objects.equals(end, dateRange.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
