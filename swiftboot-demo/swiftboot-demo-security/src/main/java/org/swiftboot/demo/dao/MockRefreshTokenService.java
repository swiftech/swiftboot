package org.swiftboot.demo.dao;

import org.swiftboot.common.auth.token.RefreshToken;
import org.swiftboot.security.RefreshTokenService;

import java.util.HashMap;
import java.util.Map;

/**
 * Mocking refresh token storage and data operations.
 * @deprecated
 */
public class MockRefreshTokenService implements RefreshTokenService {

    private final Map<String, RefreshToken> userMap = new HashMap<>();
    private final Map<String, RefreshToken> tokenMap = new HashMap<>();

    @Override
    public void saveRefreshToken(String userId, String token, Long expiresAt) {
        RefreshToken refreshToken = new RefreshToken(token, expiresAt);
        userMap.put(userId, refreshToken);
        tokenMap.put(token, refreshToken);
    }

    @Override
    public boolean revokeRefreshToken(String userId) {
        if (!userMap.containsKey(userId)) {
            return false;
        }
        tokenMap.remove(userMap.get(userId).tokenValue());
        userMap.remove(userId);
        return true;
    }

    @Override
    public boolean isValidRefreshToken(String refreshToken) {
        return tokenMap.containsKey(refreshToken);
    }
}
