package org.swiftboot.security;

/**
 * DAO to manage user and it's Refresh Token.
 *
 * @since 3.0.0
 */
public interface RefreshTokenDao {

    void saveRefreshToken(String username, String refreshToken);

    boolean revokeRefreshToken(String username);

    boolean isValidRefreshToken(String refreshToken);

}
