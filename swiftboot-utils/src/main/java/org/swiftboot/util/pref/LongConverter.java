package org.swiftboot.util.pref;

/**
 * Converter for object save as long.
 *
 * @author swiftech
 */
public abstract class LongConverter<O> implements Converter<O, Long> {

    @Override
    public Class<Long> getSaveAs() {
        return Long.class;
    }
}
