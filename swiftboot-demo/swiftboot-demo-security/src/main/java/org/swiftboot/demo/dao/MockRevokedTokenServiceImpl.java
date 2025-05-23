package org.swiftboot.demo.dao;

import org.swiftboot.security.RevokedTokenService;

import java.util.HashMap;
import java.util.Map;

/**
 * Mocking revoked token storage and data operations.
 * @deprecated
 */
public class MockRevokedTokenServiceImpl implements RevokedTokenService {

    private final Map<String, String> userTokenMap = new HashMap<>();

    @Override
    public boolean revokeToken(String token) {
        if (!userTokenMap.containsKey(token)) {
            userTokenMap.put(token, null);
            return true;
        }
        return false;
    }

    @Override
    public boolean isRevoked(String token) {
        return userTokenMap.containsKey(token);
    }

    @Override
    public void removeToken(String token) {
        userTokenMap.remove(token);
    }
}
