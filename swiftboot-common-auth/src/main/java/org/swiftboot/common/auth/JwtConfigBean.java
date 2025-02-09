package org.swiftboot.common.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @since 3.0.0
 * @see JwtTokenProvider
 */
@Configuration
@ConfigurationProperties("swiftboot.auth.jwt")
public class JwtConfigBean {
    private String secret;
    private long expirationSeconds;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpirationSeconds() {
        return expirationSeconds;
    }

    public void setExpirationSeconds(long expirationSeconds) {
        this.expirationSeconds = expirationSeconds;
    }
}
