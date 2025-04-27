package org.swiftboot.common.auth.service;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.common.auth.JwtService;
import org.swiftboot.common.auth.config.JwtConfigBean;
import org.swiftboot.common.auth.token.JwtAuthentication;
import org.swiftboot.common.auth.token.AccessToken;
import org.swiftboot.common.auth.token.RefreshToken;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In memory JWT service implementation to handle access token refreshing without any storage.
 *
 * @since 3.0
 */
public class InMemoryJwtService implements JwtService {

    @Resource
    private JwtConfigBean jwtConfig;

    private static final Logger log = LoggerFactory.getLogger(InMemoryJwtService.class);

    // access token -> authentication object
    private final Map<String, JwtAuthentication> accessTokenMap = new ConcurrentHashMap<>();
    // refresh token -> authentication object
    private final Map<String, JwtAuthentication> refreshTokenMap = new ConcurrentHashMap<>();

    @Override
    public void saveJwtAuthentication(JwtAuthentication jwtAuthentication) {
        accessTokenMap.put(jwtAuthentication.getAccessToken().tokenValue(), jwtAuthentication);
        refreshTokenMap.put(jwtAuthentication.getRefreshToken().tokenValue(), jwtAuthentication);
    }

    @Override
    public void saveJwtAuthentication(AccessToken accessToken, RefreshToken refreshToken) {
        JwtAuthentication jwtAuthentication = new JwtAuthentication(accessToken, refreshToken);
        accessTokenMap.put(accessToken.tokenValue(), jwtAuthentication);
        refreshTokenMap.put(refreshToken.tokenValue(), jwtAuthentication);
    }

    @Override
    public void saveJwtAuthentication(String accessToken, Long accessTokenExpiresAt, String refreshToken, Long refreshTokenExpiresAt) {
        JwtAuthentication jwtAuthentication = new JwtAuthentication(accessToken, accessTokenExpiresAt, refreshToken, refreshTokenExpiresAt);
        accessTokenMap.put(accessToken, jwtAuthentication);
        refreshTokenMap.put(refreshToken, jwtAuthentication);
        if (log.isInfoEnabled()) log.info("Saved JWT access token and refresh token: %s".formatted(accessToken));
    }

    @Override
    public boolean revokeAuthenticationByAccessToken(String accessToken) {
        if (log.isDebugEnabled())
            log.debug("Revoke JWT access token: " + StringUtils.abbreviateMiddle(accessToken, ".", 64));
        JwtAuthentication jwtAuthentication = accessTokenMap.get(accessToken);
        if (jwtAuthentication != null) {
            if (jwtConfig.isRefreshRevokeType()) {
                refreshTokenMap.remove(jwtAuthentication.getRefreshToken().tokenValue());
            }
            accessTokenMap.remove(accessToken);
            return true;
        }
        return false;
    }

    @Override
    public boolean revokeAuthenticationByRefreshToken(String refreshToken) {
        if (log.isDebugEnabled())
            log.debug("Revoke JWT access token by refresh token: " + StringUtils.abbreviateMiddle(refreshToken, ".", 64));
        JwtAuthentication jwtAuthentication = refreshTokenMap.get(refreshToken);
        if (jwtAuthentication != null) {
            accessTokenMap.remove(jwtAuthentication.getAccessToken().tokenValue());
            refreshTokenMap.remove(refreshToken);
            return true;
        }
        return false;
    }

    @Override
    public boolean isRevokedRefreshToken(String refreshToken) {
        return !refreshTokenMap.containsKey(refreshToken);
    }

    @Override
    public boolean isRevokedAccessToken(String accessToken) {
        return !accessTokenMap.containsKey(accessToken);
    }

    @Override
    public JwtAuthentication getJwtAuthentication(String accessToken) {
        return accessTokenMap.get(accessToken);
    }
}
