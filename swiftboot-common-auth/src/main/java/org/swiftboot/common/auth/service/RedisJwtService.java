package org.swiftboot.common.auth.service;

import org.apache.commons.lang3.NotImplementedException;
import org.swiftboot.common.auth.JwtService;
import org.swiftboot.common.auth.token.JwtAuthentication;
import org.swiftboot.common.auth.token.AccessToken;
import org.swiftboot.common.auth.token.RefreshToken;

/**
 * TODO implement with Redis storage.
 * @since 3.0
 */
public class RedisJwtService implements JwtService {

    @Override
    public void saveJwtAuthentication(JwtAuthentication jwtAuthentication) {
        throw new NotImplementedException("Not implemented");
    }

    @Override
    public void saveJwtAuthentication(AccessToken accessToken, RefreshToken refreshToken) {
        throw new NotImplementedException("Not implemented");
    }

    @Override
    public void saveJwtAuthentication(String accessToken, Long accessTokenExpiresAt, String refreshToken, Long refreshTokenExpiresAt) {
        throw new NotImplementedException("Not implemented");
    }

    @Override
    public boolean revokeAuthenticationByAccessToken(String accessToken) {
        throw new NotImplementedException("Not implemented");
    }

    @Override
    public boolean revokeAuthenticationByRefreshToken(String refreshToken) {
        throw new NotImplementedException("Not implemented");
    }

    @Override
    public boolean isRevokedRefreshToken(String refreshToken) {
        throw new NotImplementedException("Not implemented");
    }

    @Override
    public boolean isRevokedAccessToken(String accessToken) {
        throw new NotImplementedException("Not implemented");
    }

    @Override
    public JwtAuthentication getJwtAuthentication(String accessToken) {
        throw new NotImplementedException("Not implemented");
    }
}
