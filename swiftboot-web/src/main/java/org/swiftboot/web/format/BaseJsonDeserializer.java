package org.swiftboot.web.format;

import com.fasterxml.jackson.databind.JsonDeserializer;

import java.time.format.DateTimeFormatter;

/**
 * @since 3.2
 * @param <T>
 */
public abstract class BaseJsonDeserializer<T> extends JsonDeserializer<T> {

    protected final DateTimeFormatter formatter;

    public BaseJsonDeserializer(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

}
