package org.swiftboot.util.pref;

/**
 * Converter for object save as string.
 *
 * @author swiftech
 */
public abstract class StringConverter<O> implements Converter<O, String> {

    @Override
    public Class<String> getSaveAs() {
        return String.class;
    }
}
