package org.swiftboot.util.pref;

/**
 * Convert an object to basic type of value which can be saved to or retrieved from Java preferences.
 *
 * @param <O> type of user object
 * @param <P> type of saved preference, only String, Integer, Long or byte array are supported.
 * @since 2.3
 */
public interface Converter<O, P> {

    /**
     * Deserialize from saved preference value to user object.
     *
     * @param prefValue if no preference exists, this method must not return null.
     * @return
     */
    O deserialize(P prefValue);

    /**
     *
     * @param valueObject if null, nothing will be saved in preference.
     * @return
     */
    P serialize(O valueObject);

    /**
     * Type of value in preferences, only String, Integer, Long or byte array are supported.
     *
     * @return
     */
    Class<P> getSaveAs();

}
