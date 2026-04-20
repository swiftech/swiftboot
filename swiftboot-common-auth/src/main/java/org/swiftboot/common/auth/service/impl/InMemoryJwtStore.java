package org.swiftboot.common.auth.service.impl;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.common.auth.config.JwtConfigBean;
import org.swiftboot.common.auth.service.JwtService;
import org.swiftboot.common.auth.service.JwtStore;
import org.swiftboot.common.auth.token.JwtAuthentication;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stores tokens in memory for developing and testing.
 *
 * @see JwtService
 * @see JwtServiceImpl
 * @since 3.1
 */
public class InMemoryJwtStore implements JwtStore {

    private static final Logger log = LoggerFactory.getLogger(InMemoryJwtStore.class);

    @Resource
    private JwtConfigBean jwtConfig;

    public InMemoryJwtStore() {
        log.warn("In memory jwt store is enabled, replace with a persistent jwt store for production");
    }

    // access token -> authentication object
    private final Map<String, JwtAuthentication> accessTokenMap = new ConcurrentHashMap<>();
    // refresh token -> authentication object
    private final Map<String, JwtAuthentication> refreshTokenMap = new ConcurrentHashMap<>();


    @Override
    public void save(JwtAuthentication jwtAuthentication) {
        log.warn("Save authentication tokens into memory");
        if (jwtConfig.isRefreshRevokeType()) {
            accessTokenMap.put(jwtAuthentication.getAccessToken().getTokenValue(), jwtAuthentication);
            refreshTokenMap.put(jwtAuthentication.getRefreshToken().getTokenValue(), jwtAuthentication);
        }
        else {
            accessTokenMap.put(jwtAuthentication.getAccessToken().getTokenValue(), jwtAuthentication);
        }
    }

    @Override
    public void removeAccessToken(String accessToken) {
        accessTokenMap.remove(accessToken);
        JwtAuthentication ja = accessTokenMap.get(accessToken);
        refreshTokenMap.remove(ja.getRefreshToken().getTokenValue());
    }

    @Override
    public void removeRefreshToken(String refreshToken) {
        refreshTokenMap.remove(refreshToken);
    }

    @Override
    public JwtAuthentication loadByAccessToken(String accessToken) {
        log.warn("Load authentication tokens from memory");
        return accessTokenMap.get(accessToken);
    }

    @Override
    public JwtAuthentication loadByRefreshToken(String refreshToken) {
        log.warn("Load authentication tokens from memory");
        return refreshTokenMap.get(refreshToken);
    }
}
