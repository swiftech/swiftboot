package org.swiftboot.data.util;

import org.hibernate.type.BasicType;
import org.hibernate.type.Type;
import org.hibernate.type.descriptor.java.JdbcTimestampJavaType;
import org.hibernate.type.descriptor.java.LocalDateTimeJavaType;
import org.hibernate.type.descriptor.java.LongJavaType;

import java.time.LocalDateTime;
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
            else {
                throw new RuntimeException("Unsupported basic type: " + type.getClass());
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
