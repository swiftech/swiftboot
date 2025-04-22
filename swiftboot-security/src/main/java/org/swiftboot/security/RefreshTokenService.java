package org.swiftboot.security;

/**
 * DAO to manage user and it's Refresh Token.
 * @deprecated
 * @since 3.0.0
 */
public interface RefreshTokenService {

    /**
     * Save refresh token information.
     *
     * @param userId
     * @param refreshToken
     * @param expiresAt
     */
    void saveRefreshToken(String userId, String refreshToken, Long expiresAt);

    boolean isValidRefreshToken(String refreshToken);

    boolean revokeRefreshToken(String userId);

}
