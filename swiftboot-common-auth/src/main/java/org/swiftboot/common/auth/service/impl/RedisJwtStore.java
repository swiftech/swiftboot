package org.swiftboot.common.auth.service.impl;

import org.swiftboot.common.auth.service.JwtStore;
import org.swiftboot.common.auth.token.JwtAuthentication;

/**
 * @since 3.1
 */
public class RedisJwtStore implements JwtStore {

    @Override
    public void save(JwtAuthentication jwtAuthentication) {

    }

    @Override
    public void removeAccessToken(String accessToken) {

    }

    @Override
    public void removeRefreshToken(String refreshToken) {

    }

    @Override
    public JwtAuthentication loadByAccessToken(String accessToken) {
        return null;
    }

    @Override
    public JwtAuthentication loadByRefreshToken(String refreshToken) {
        return null;
    }
}
