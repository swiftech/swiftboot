package org.swiftboot.auth.service.impl;

import org.apache.commons.lang3.NotImplementedException;
import org.swiftboot.auth.model.JwtAuthentication;
import org.swiftboot.auth.service.JwtService;

/**
 * TODO implement with Redis storage.
 * @since 3.0
 */
public class JwtServiceRedisImpl implements JwtService {

    @Override
    public void saveJwtAuthentication(String accessToken, Long accessTokenExpiresAt, String refreshToken, Long refreshTokenExpiresAt) {
        throw new NotImplementedException("Not implemented");
    }

    @Override
    public void revokeRefreshToken(String refreshToken) {
        throw new NotImplementedException("Not implemented");
    }

    @Override
    public JwtAuthentication getJwtAuthentication(String accessToken) {
        throw new NotImplementedException("Not implemented");
    }
}
