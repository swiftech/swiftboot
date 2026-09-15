package org.swiftboot.common.auth.event;

import org.springframework.context.ApplicationEvent;

import java.util.Locale;

/**
 * @since 3.2
 */
public abstract class BaseSignEvent extends ApplicationEvent {
    protected String userId;
    protected Locale locale;

    public BaseSignEvent(Object source) {
        super(source);
    }

    public BaseSignEvent(Object source, String userId, Locale locale) {
        super(source);
        this.userId = userId;
        this.locale = locale;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
