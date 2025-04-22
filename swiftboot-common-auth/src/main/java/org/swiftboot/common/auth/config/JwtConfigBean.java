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
     */
    private String revokeType = "direct";

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

    public String getRevokeType() {
        return revokeType;
    }

    public void setRevokeType(String revokeType) {
        this.revokeType = revokeType;
    }
}
