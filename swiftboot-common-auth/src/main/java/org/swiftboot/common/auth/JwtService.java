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

    /**
     *
     * @param jwtAuthentication
     */
    void saveJwtAuthentication(JwtAuthentication jwtAuthentication);

    /**
     *
     * @param accessToken
     * @param refreshToken
     */
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
     * for `direct mode`: Revoke access token directly, revoked access token MUST be stored for validating everytime in filter.
     * for `refresh mode`: Revoke a refresh token associated with access token to force user to re-login.
     *
     * @param accessToken
     * @return
     */
    boolean revokeAuthenticationByAccessToken(String accessToken);

    /**
     * Revoke a refresh token to force user to re-login.
     * Only for `refresh` revoke mode.
     *
     * @param refreshToken
     * @return
     */
    boolean revokeAuthenticationByRefreshToken(String refreshToken);

    /**
     * Validate if a refresh token is revoked.
     *
     * @param refreshToken
     * @return
     */
    boolean isRevokedRefreshToken(String refreshToken);

    /**
     * Validate if an access token is revoked directly.
     *
     * @param accessToken
     * @return
     */
    boolean isRevokedAccessToken(String accessToken);

    /**
     * Get more authentication information by access token.
     *
     * @param accessToken
     * @return
     */
    JwtAuthentication getJwtAuthentication(String accessToken);

}
