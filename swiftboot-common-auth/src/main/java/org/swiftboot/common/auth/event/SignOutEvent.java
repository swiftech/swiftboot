package org.swiftboot.common.auth.event;

import java.util.Locale;

/**
 * Emitted when user sign-in successfully.
 *
 * @since 3.2
 */
public class SignOutEvent extends BaseSignEvent {
    public SignOutEvent(Object source) {
        super(source);
    }

    public SignOutEvent(Object source, String userId, Locale locale) {
        super(source, userId, locale);
    }
}
