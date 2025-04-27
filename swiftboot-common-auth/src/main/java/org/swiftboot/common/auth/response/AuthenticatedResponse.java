package org.swiftboot.common.auth.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.swiftboot.common.auth.token.Authenticated;
import org.swiftboot.web.response.Response;

/**
 * The {@link Authenticated} in AuthenticatedResponse will be saved to session storage automatically by
 * AOP advices.
 *
 * @author swiftech
 * @since 2.2
 */
public class AuthenticatedResponse<R, S extends Authenticated> extends Response<R> {

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
