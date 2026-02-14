package org.swiftboot.common.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.swiftboot.common.auth.JwtTokenProvider;

/**
 *
 * @since 3.0.0
 * @see JwtTokenProvider
 */
@Configuration
@ConfigurationProperties("swiftboot.auth.jwt")
public class JwtConfigBean {

    /**
     * JWT secret
     */
    private String secret;

    /**
     * default is 10 minutes.
     */
    private long accessTokenExpirationSeconds = 10 * 60;

    /**
     * default is 24 hours.
     */
    private long refreshTokenExpirationSeconds = 24 * 60 * 60;

    /**
     * `direct` | `refresh` , default is `direct`
     * `direct`: stores revoked access token and checks user's token everytime in filter.
     * `refresh`: do not check user's token but force client to refresh access token by refresh token.
     */
    private String revokeType = "direct";

    /**
     * `immutable` | `rolling`
     * How to handle the used refresh token after refreshing access token, only works when revokeType is `refresh`:
     * immutable: keep the original refresh token until it expires.
     * rolling: re-generate new refresh token everytime a new access token is refreshed.
     */
    private String refreshMode = "immutable";

    /**
     * `memory` | `redis`
     * memory: stores tokens in memory, for testing only.
     * redis: stores tokens in redis.
     * default is `memory`.
     * To customize the store, like in a database, do not set this property, just implement {@code JwtStore} and register as a @Service or a @Component.
     *
     * @see org.swiftboot.common.auth.service.JwtStore
     */
    private String storeMode = "memory";


    public boolean isDirectRevokeType() {
        return "direct".equalsIgnoreCase(revokeType);
    }

    public boolean isRefreshRevokeType() {
        return "refresh".equalsIgnoreCase(revokeType);
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getAccessTokenExpirationSeconds() {
        return accessTokenExpirationSeconds;
    }

    public void setAccessTokenExpirationSeconds(long accessTokenExpirationSeconds) {
        this.accessTokenExpirationSeconds = accessTokenExpirationSeconds;
    }

    public long getRefreshTokenExpirationSeconds() {
        return refreshTokenExpirationSeconds;
    }

    public void setRefreshTokenExpirationSeconds(long refreshTokenExpirationSeconds) {
        this.refreshTokenExpirationSeconds = refreshTokenExpirationSeconds;
    }

    public String getRefreshMode() {
        return refreshMode;
    }

    public void setRefreshMode(String refreshMode) {
        this.refreshMode = refreshMode;
    }

    public String getRevokeType() {
        return revokeType;
    }

    public void setRevokeType(String revokeType) {
        this.revokeType = revokeType;
    }

    public String getStoreMode() {
        return storeMode;
    }

    public void setStoreMode(String storeMode) {
        this.storeMode = storeMode;
    }
}
