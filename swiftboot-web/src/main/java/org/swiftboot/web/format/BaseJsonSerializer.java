package org.swiftboot.web.format;

import com.fasterxml.jackson.databind.JsonSerializer;

import java.time.format.DateTimeFormatter;

/**
 * @since 3.2
 * @param <T>
 */
public abstract class BaseJsonSerializer<T> extends JsonSerializer<T> {

    protected final DateTimeFormatter formatter;

    public BaseJsonSerializer(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }


}
