package org.swiftboot.common.auth.token;

/**
 * Stored information if authenticated under JWT mode.
 *
 * @since 3.0
 */
public class JwtAuthentication implements Authenticated {

    private final AccessToken accessToken;

    private final RefreshToken refreshToken;

    public JwtAuthentication(AccessToken accessToken, RefreshToken refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public JwtAuthentication(String accessToken, Long accessTokenExpiresAt) {
        this.accessToken = new AccessToken(accessToken, accessTokenExpiresAt);
        this.refreshToken = null;
    }

    public JwtAuthentication(String accessToken, Long accessTokenExpiresAt, String refreshToken, Long refreshTokenExpiresAt) {
        this.accessToken = new AccessToken(accessToken, accessTokenExpiresAt);
        this.refreshToken = new RefreshToken(refreshToken, refreshTokenExpiresAt);
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

}
