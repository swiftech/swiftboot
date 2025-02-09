package org.swiftboot.util.pref;

/**
 * Converter for object save as integer.
 *
 * @author swiftech
 */
public abstract class IntegerConverter<O> implements Converter<O, Integer> {

    @Override
    public Class<Integer> getSaveAs() {
        return Integer.class;
    }
}
