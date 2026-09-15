package org.swiftboot.common.auth.event;

import java.util.Locale;

/**
 * Emitted when user sign-up successfully.
 *
 * @since 3.2
 */
public class SignUpEvent extends BaseSignEvent {

    public SignUpEvent(Object source) {
        super(source);
    }

    public SignUpEvent(Object source, String userId, Locale locale) {
        super(source, userId, locale);
    }
}
