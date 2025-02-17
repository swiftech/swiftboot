package org.swiftboot.demo.dao;

import org.swiftboot.security.RefreshTokenDao;

import java.util.HashMap;
import java.util.Map;

/**
 * Mocking refresh token storage and data operations.
 */
public class MockRefreshTokenDaoImpl implements RefreshTokenDao {

    private final Map<String, String> map = new HashMap<>();

    @Override
    public void saveRefreshToken(String username, String refreshToken) {
        map.put(username, refreshToken);
    }

    @Override
    public boolean revokeRefreshToken(String username) {
        if (!map.containsKey(username)) {
            return false;
        }
        map.remove(username);
        return true;
    }

    @Override
    public boolean isValidRefreshToken(String refreshToken) {
        return map.containsKey(refreshToken);
    }
}
