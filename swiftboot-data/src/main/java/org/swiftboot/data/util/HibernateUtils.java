package org.swiftboot.data.util;

import org.hibernate.type.BasicType;
import org.hibernate.type.Type;
import org.hibernate.type.descriptor.java.JdbcTimestampJavaType;
import org.hibernate.type.descriptor.java.LocalDateTimeJavaType;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @author swiftech
 * @since 2.0.2
 */
public class HibernateUtils {

    /**
     * create now time by specified Hibernate data type.
     *
     * @param type
     * @return
     */
    public static Object nowByType(Type type) {
        if (type instanceof BasicType bt) {
            if (bt.getJavaType() == Long.class) {
                return System.currentTimeMillis();
            }
            else if (bt.getJavaType() == LocalDateTime.class) {
                return LocalDateTime.now();
            }
            else if (bt.getJavaType() == LocalDate.class) {
                return LocalDate.now();
            }
            else if (bt.getJavaType() == Timestamp.class) {
                return new Timestamp(System.currentTimeMillis());
            }
            else {
                throw new RuntimeException("Unsupported basic type: %s with java type: %s".formatted(bt.getClass(), bt.getJavaType()));
            }
        }
        else if (type instanceof LocalDateTimeJavaType) {
            return LocalDateTime.now();
        }
        else if (type instanceof JdbcTimestampJavaType) {
            return new Date();
        }
        else {
            throw new RuntimeException("Type of value is not supported: " + type.getClass());
        }
    }
}
