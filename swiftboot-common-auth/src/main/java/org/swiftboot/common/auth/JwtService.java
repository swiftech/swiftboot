package org.swiftboot.common.auth;

import org.swiftboot.common.auth.token.JwtAuthentication;
import org.swiftboot.common.auth.token.AccessToken;
import org.swiftboot.common.auth.token.RefreshToken;

/**
 * Saving, getting and revoking Refresh Token to control the short-time Access Token.
 *
 * @since 3.0
 */
public interface JwtService {

    void saveJwtAuthentication(JwtAuthentication jwtAuthentication);

    void saveJwtAuthentication(AccessToken accessToken, RefreshToken refreshToken);

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
     * @param accessToken
     * @return
     */
    boolean revokeAuthenticationByAccessToken(String accessToken);

    boolean revokeAuthenticationByRefreshToken(String refreshToken);

    boolean isRevokedRefreshToken(String refreshToken);

    /**
     * Get more authentication information by access token.
     *
     * @param accessToken
     * @return
     */
    JwtAuthentication getJwtAuthentication(String accessToken);

}
