package org.swiftboot.security;

/**
 * @since 3.0.0
 */
public interface RevokedTokenDao {

    /**
     * Revoke token directly, this will prevent accessing with valid token.
     *
     * @param token
     * @return revoked or not
     */
    boolean revokeToken(String token);

    /**
     * Verify whether the token is valid.
     *
     * @param token
     * @return
     */
    boolean isRevoked(String token);

    /**
     * Remove token to reduce storage when like token is expired.
     *
     * @param token
     */
    void removeToken(String token);
}
