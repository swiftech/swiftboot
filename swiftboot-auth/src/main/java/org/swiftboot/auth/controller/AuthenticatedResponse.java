package org.swiftboot.auth.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.swiftboot.auth.service.Session;
import org.swiftboot.web.result.HttpResponse;

/**
 * The {@link Session} in AuthenticatedResponse will be saved to session storage automatically by
 * {@link org.swiftboot.auth.interceptor.UserSessionResponseAdvice}
 *
 * @author swiftech
 * @since 2.2
 * @see org.swiftboot.auth.interceptor.UserSessionResponseAdvice
 */
public class AuthenticatedResponse<T> extends HttpResponse<T> {

    @JsonIgnore
    private Session userSession;

    public AuthenticatedResponse(Session userSession) {
        this.userSession = userSession;
    }

    public AuthenticatedResponse(T result, Session userSession) {
        super(result);
        this.userSession = userSession;
    }

    public Session getUserSession() {
        return userSession;
    }

    public void setUserSession(Session userSession) {
        this.userSession = userSession;
    }
}
