package org.swiftboot.common.auth.event;

import java.util.Locale;

/**
 * Emitted when user sign-in successfully.
 *
 * @since 3.2
 */
public class SignInEvent extends BaseSignEvent {

    public SignInEvent(Object source) {
        super(source);
    }

    public SignInEvent(Object source, String userId, Locale locale) {
        super(source, userId, locale);
    }

}
