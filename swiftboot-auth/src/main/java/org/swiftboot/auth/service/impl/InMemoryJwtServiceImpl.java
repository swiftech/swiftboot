package org.swiftboot.auth.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.auth.model.JwtAuthentication;
import org.swiftboot.auth.service.JwtService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In memory JWT service implementation to handle access token refreshing without any storage.
 *
 * @since 3.0
 */
public class InMemoryJwtServiceImpl implements JwtService {

    private static final Logger log = LoggerFactory.getLogger(InMemoryJwtServiceImpl.class);

    private final Map<String, JwtAuthentication> jwtSessionMap = new ConcurrentHashMap<>();

    @Override
    public void saveJwtAuthentication(String accessToken, Long accessTokenExpiresAt, String refreshToken, Long refreshTokenExpiresAt) {
        JwtAuthentication jwtAuthentication = new JwtAuthentication(accessToken, accessTokenExpiresAt, refreshToken, refreshTokenExpiresAt);
        jwtSessionMap.put(accessToken, jwtAuthentication);
        log.info("Saved JWT access token and refresh token: " + accessToken);
    }

    @Override
    public void revokeRefreshToken(String refreshToken) {
        jwtSessionMap.remove(refreshToken);
    }

    @Override
    public JwtAuthentication getJwtAuthentication(String accessToken) {
        return jwtSessionMap.get(accessToken);
    }
}
