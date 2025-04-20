package org.swiftboot.auth.service;

import org.swiftboot.auth.model.JwtAuthentication;

/**
 * Saving, getting and revoking Refresh Token to control the short-time Access Token.
 *
 * @since 3.0
 */
public interface JwtService {

    /**
     * Save access token and refresh token, if data for the access token already exists, it will be overridden with new one.
     *
     * @param accessToken
     * @param accessTokenExpiresAt
     * @param refreshToken
     * @param refreshTokenExpiresAt
     */
    void saveJwtAuthentication(String accessToken, Long accessTokenExpiresAt, String refreshToken, Long refreshTokenExpiresAt);


    /**
     * Revoke refresh token to force user to re-login.
     *
     * @param refreshToken
     */
    void revokeRefreshToken(String refreshToken);

    /**
     * Get more authentication information by access token.
     *
     * @param accessToken
     * @return
     */
    JwtAuthentication getJwtAuthentication(String accessToken);

}
