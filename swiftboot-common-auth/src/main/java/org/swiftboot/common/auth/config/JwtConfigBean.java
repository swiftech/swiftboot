package org.swiftboot.common.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.swiftboot.common.auth.JwtTokenProvider;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @see JwtTokenProvider
 * @since 3.0.0
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
     * redis: stores tokens in Redis.
     * default is `memory`.
     * To customize the store, like in a database, do not set this property, just implement {@code JwtStore} and register as a @Service or a @Component.
     *
     * @see org.swiftboot.common.auth.service.JwtStore
     */
    private String storeMode = "memory";

    /**
     * Use cookie to return access token and refresh token.
     */
    private Boolean useCookie = true;

    /**
     * Cookie config for access token.
     */
    private CookieConfigBean accessTokenCookie;

    /**
     * Cookie config for refresh token.
     */
    private CookieConfigBean refreshTokenCookie;

    /**
     * Specific config override the default config for client.
     * The identifier for clients is defined in the BaseAuthRequest.
     * use following methods to determine the final config for clients:
     * determineAccessTokenExpirationSeconds()
     * determineRefreshTokenExpirationSeconds()
     * determineAccessTokenCookie()
     * determineRefreshTokenCookie()
     *
     * @see org.swiftboot.common.auth.request.BaseAuthRequest
     */
    private Map<String, ClientOverride> clientOverrides = new HashMap<>();

    public long determineAccessTokenExpirationSeconds(String clientSource) {
        if (clientOverrides.containsKey(clientSource)) {
            return clientOverrides.get(clientSource).getAccessTokenExpirationSeconds();
        }
        return accessTokenExpirationSeconds;
    }

    public long determineRefreshTokenExpirationSeconds(String clientSource) {
        if (clientOverrides.containsKey(clientSource)) {
            return clientOverrides.get(clientSource).getRefreshTokenExpirationSeconds();
        }
        return refreshTokenExpirationSeconds;
    }

    public CookieConfigBean determineAccessTokenCookie(String clientSource) {
        if (clientOverrides.containsKey(clientSource)) {
            CookieConfigBean atc = clientOverrides.get(clientSource).getAccessTokenCookie();
            if (atc != null) {
                return atc;
            }
        }
        return accessTokenCookie;
    }

    public CookieConfigBean determineRefreshTokenCookie(String clientSource) {
        if (clientOverrides.containsKey(clientSource)) {
            CookieConfigBean rtc = clientOverrides.get(clientSource).getRefreshTokenCookie();
            if (rtc != null) {
                return rtc;
            }
        }
        return refreshTokenCookie;
    }

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

    public Boolean getUseCookie() {
        return useCookie;
    }

    public void setUseCookie(Boolean useCookie) {
        this.useCookie = useCookie;
    }

    public CookieConfigBean getAccessTokenCookie() {
        return accessTokenCookie;
    }

    public void setAccessTokenCookie(CookieConfigBean accessTokenCookie) {
        this.accessTokenCookie = accessTokenCookie;
    }

    public CookieConfigBean getRefreshTokenCookie() {
        return refreshTokenCookie;
    }

    public void setRefreshTokenCookie(CookieConfigBean refreshTokenCookie) {
        this.refreshTokenCookie = refreshTokenCookie;
    }

    public Map<String, ClientOverride> getClientOverrides() {
        return clientOverrides;
    }

    public void setClientOverrides(Map<String, ClientOverride> clientOverrides) {
        this.clientOverrides = clientOverrides;
    }

    /**
     *
     */
    public static class ClientOverride {
        /**
         * default is 10 minutes.
         */
        private long accessTokenExpirationSeconds = 10 * 60;

        /**
         * default is 24 hours.
         */
        private long refreshTokenExpirationSeconds = 24 * 60 * 60;

        private CookieConfigBean accessTokenCookie;

        private CookieConfigBean refreshTokenCookie;

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

        public CookieConfigBean getAccessTokenCookie() {
            return accessTokenCookie;
        }

        public void setAccessTokenCookie(CookieConfigBean accessTokenCookie) {
            this.accessTokenCookie = accessTokenCookie;
        }

        public CookieConfigBean getRefreshTokenCookie() {
            return refreshTokenCookie;
        }

        public void setRefreshTokenCookie(CookieConfigBean refreshTokenCookie) {
            this.refreshTokenCookie = refreshTokenCookie;
        }
    }
}
