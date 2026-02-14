package org.swiftboot.common.auth.service;

import org.swiftboot.common.auth.token.JwtAuthentication;

/**
 * Implement your own custom JWT store, there is no need to verify the tokens, just save or load the tokens.
 *
 * @since 3.1
 */
public interface JwtStore {

    /**
     * Save access token and refresh token.
     *
     * @param jwtAuthentication
     */
    void save(JwtAuthentication jwtAuthentication);

    /**
     * Remove access token from store, works for revokeType=direct
     * Do NOT throw exception when token is expired since it is allowed in some scenarios like user logout with expired token.
     *
     * @param accessToken
     */
    void removeAccessToken(String accessToken);

    /**
     * Remove refresh token from store, works for revokeType=refresh
     * Do NOT throw exception when token is expired since it is allowed in some scenarios like user logout with expired token.
     *
     * @param refreshToken
     */
    void removeRefreshToken(String refreshToken);

    /**
     * Load JWT from store by access token.
     *
     * @param accessToken
     * @return null or JwtAuthentication.refreshToken is null means the user-login has been revoked.
     */
    JwtAuthentication loadByAccessToken(String accessToken);

    /**
     * Load JWT from store by refresh token.
     *
     * @param refreshToken
     * @return null or JwtAuthentication.refreshToken is null means the user-login has been revoked.
     */
    JwtAuthentication loadByRefreshToken(String refreshToken);
}
