package org.swiftboot.auth.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.swiftboot.common.auth.token.Authenticated;
import org.swiftboot.auth.model.Session;
import org.swiftboot.web.result.HttpResponse;

/**
 * The {@link Session} in AuthenticatedResponse will be saved to session storage automatically by
 * {@link org.swiftboot.auth.interceptor.UserSessionResponseAdvice}
 *
 * @author swiftech
 * @since 2.2
 * @see org.swiftboot.auth.interceptor.UserSessionResponseAdvice
 * @see org.swiftboot.auth.interceptor.UserJwtResponseAdvice
 */
public class AuthenticatedResponse<R, S extends Authenticated> extends HttpResponse<R> {

    @JsonIgnore
    private S authenticated;

    public AuthenticatedResponse(S authenticated) {
        this.authenticated = authenticated;
    }

    public AuthenticatedResponse(R result, S authenticated) {
        super(result);
        this.authenticated = authenticated;
    }

    public S getAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(S authenticated) {
        this.authenticated = authenticated;
    }
}
