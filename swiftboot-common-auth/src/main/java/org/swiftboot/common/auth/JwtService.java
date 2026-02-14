package org.swiftboot.common.auth;

import org.apache.commons.lang3.StringUtils;
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
     *
     * Only for `refresh` revoke mode and be used in the `rolling` refresh mode for revoke the used refresh token.
     *
     * @param refreshToken
     * @return true if the token is revoked successfully
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


    static String abbreviateToken(String token) {
        return StringUtils.abbreviateMiddle(token, "...", 64);
    }

}
