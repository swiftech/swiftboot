package org.swiftboot.data.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.sql.Date;
import java.time.YearMonth;

/**
 * Convert {@link java.time.YearMonth} to {@link java.sql.Date} and vice versa to support saving to database.
 * add following annotation to the YearMonth type attribute:
 * {@code @Convert(converter = YearMonthAttributeConverter.class)}
 *
 * @since 3.1.0
 */
@Converter()
public class YearMonthAttributeConverter implements AttributeConverter<YearMonth, Date> {

    @Override
    public Date convertToDatabaseColumn(YearMonth yearMonth) {
        if (yearMonth == null) {
            return null;
        }
        return Date.valueOf(yearMonth.atDay(1));
    }

    @Override
    public YearMonth convertToEntityAttribute(Date dbData) {
        if (dbData == null) {
            return null;
        }
        return YearMonth.from(dbData.toLocalDate());
    }
}
