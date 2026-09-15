package org.swiftboot.data.util;

import org.hibernate.type.BasicType;
import org.hibernate.type.Type;
import org.hibernate.type.descriptor.java.InstantJavaType;
import org.hibernate.type.descriptor.java.JdbcTimestampJavaType;
import org.hibernate.type.descriptor.java.LocalDateTimeJavaType;
import org.hibernate.type.descriptor.java.OffsetDateTimeJavaType;

import java.sql.Timestamp;
import java.time.*;
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
        if (type instanceof BasicType<?> bt) {
            if (bt.getJavaType() == Long.class) {
                return System.currentTimeMillis();
            }
            else if (bt.getJavaType() == LocalDateTime.class) {
                return LocalDateTime.now(ZoneOffset.UTC);
            }
            else if (bt.getJavaType() == LocalDate.class) {
                return LocalDate.now(ZoneOffset.UTC);
            }
            else if (bt.getJavaType() == Instant.class) {
                return Instant.now();
            }
            else if (bt.getJavaType() == OffsetDateTime.class) {
                return OffsetDateTime.now(ZoneOffset.UTC);
            }
            else if (bt.getJavaType() == Timestamp.class) {
                return new Timestamp(System.currentTimeMillis());
            }
            else {
                throw new RuntimeException("Unsupported basic type: %s with java type: %s".formatted(bt.getClass(), bt.getJavaType()));
            }
        }
        else if (type instanceof LocalDateTimeJavaType) {
            return LocalDateTime.now(ZoneOffset.UTC);
        }
        else if (type instanceof InstantJavaType) {
            return Instant.now();
        }
        else if (type instanceof OffsetDateTimeJavaType) {
            return OffsetDateTime.now(ZoneOffset.UTC);
        }
        else if (type instanceof JdbcTimestampJavaType) {
            return new Date();
        }
        else {
            throw new RuntimeException("Type of value is not supported: " + type.getClass());
        }
    }
}
