package org.swiftboot.data.util;

import org.hibernate.type.LocalDateTimeType;
import org.hibernate.type.LongType;
import org.hibernate.type.TimestampType;
import org.hibernate.type.Type;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author allen
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
        if (type instanceof LongType) {
            return System.currentTimeMillis();
        }
        else if (type instanceof LocalDateTimeType) {
            return LocalDateTime.now();
        }
        else if (type instanceof TimestampType) {
            return new Date();
        }
        else {
            throw new RuntimeException("Type of value is not supported: " + type);
        }
    }
}
