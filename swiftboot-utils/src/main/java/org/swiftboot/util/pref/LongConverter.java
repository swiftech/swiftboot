package org.swiftboot.util.pref;

/**
 * Converter for object save as long.
 *
 * @author allen
 */
public abstract class LongConverter<O> implements Converter<O, Long> {

    @Override
    public Class<Long> getSaveAs() {
        return Long.class;
    }
}
