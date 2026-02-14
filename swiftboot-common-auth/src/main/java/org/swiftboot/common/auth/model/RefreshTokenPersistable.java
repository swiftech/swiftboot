package org.swiftboot.common.auth.model;

/**
 * @since 3.1
 */
public interface RefreshTokenPersistable {

    String getRefreshToken();

    void setRefreshToken(String refreshToken);

    Long getRefreshTokenExpiresAt();

    void setRefreshTokenExpiresAt(Long expiresAt);
}
