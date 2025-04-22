package org.swiftboot.security;


import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Used for OAuth2 authentication.
 *
 * @since 3.0
 */
public class UserIdAuthenticationToken extends AbstractAuthenticationToken {

    private final Object userId;

    public UserIdAuthenticationToken(Object userId) {
        super(null);
        this.userId = userId;
        this.setAuthenticated(false);
    }

    public UserIdAuthenticationToken(Object userId, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.userId = userId;
        this.setAuthenticated(true);
    }


    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }
}
