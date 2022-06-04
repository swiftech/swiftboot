package org.swiftboot.util.pref;

/**
 * Converter for object save as bytes.
 *
 * @author allen
 */
public abstract class BytesConverter<O> implements Converter<O, byte[]> {

    @Override
    public Class<byte[]> getSaveAs() {
        return byte[].class;
    }
}
