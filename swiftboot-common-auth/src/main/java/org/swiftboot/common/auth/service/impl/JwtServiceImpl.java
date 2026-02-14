package org.swiftboot.common.auth.service.impl;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.common.auth.JwtService;
import org.swiftboot.common.auth.JwtTokenProvider;
import org.swiftboot.common.auth.config.JwtConfigBean;
import org.swiftboot.common.auth.service.JwtStore;
import org.swiftboot.common.auth.token.AccessToken;
import org.swiftboot.common.auth.token.JwtAuthentication;
import org.swiftboot.common.auth.token.RefreshToken;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.swiftboot.common.auth.JwtService.abbreviateToken;

/**
 *
 * @param
 * @since 3.1
 */
public class JwtServiceImpl implements JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtServiceImpl.class);

    @Resource
    private JwtStore jwtStore;

    @Resource
    private JwtConfigBean jwtConfig;

    @Resource
    private JwtTokenProvider jwtTokenProvider;


    @Override
    public void saveJwtAuthentication(JwtAuthentication jwtAuthentication) {
        if (jwtConfig.isRefreshRevokeType()) {
            if (jwtAuthentication.getRefreshToken() == null) {
                throw new IllegalArgumentException("revokeType is set to 'refresh' but no refresh token provided.");
            }
        }
        else {
            if (jwtAuthentication.getAccessToken() == null) {
                throw new IllegalArgumentException("No access token provided.");
            }
        }
        jwtStore.save(jwtAuthentication);
    }

    @Override
    public void saveJwtAuthentication(AccessToken accessToken, RefreshToken refreshToken) {
        JwtAuthentication jwtAuthentication = new JwtAuthentication(accessToken, refreshToken);
        this.saveJwtAuthentication(jwtAuthentication);
    }

    @Override
    public void saveJwtAuthentication(String accessToken, Long accessTokenExpiresAt, String refreshToken, Long refreshTokenExpiresAt) {
        JwtAuthentication jwtAuthentication = new JwtAuthentication(accessToken, accessTokenExpiresAt, refreshToken, refreshTokenExpiresAt);
        this.saveJwtAuthentication(jwtAuthentication);
    }

    @Override
    public boolean revokeAuthenticationByAccessToken(String accessToken) {
        if (isBlank(accessToken)) {
            log.debug("access token is blank");
            return false;
        }
        if (log.isDebugEnabled())
            log.debug("Revoke JWT authentication by access token: " + abbreviateToken(accessToken));
        try {
            jwtTokenProvider.validateToken(accessToken);
        } catch (ExpiredJwtException e) {
            // keep handling if token is expired
            log.debug("User ID from expired token: %s".formatted(e.getClaims().getSubject()));
        } catch (Exception e) {
            return false;
        }
        if (jwtConfig.isRefreshRevokeType()) {
            JwtAuthentication ja = jwtStore.loadByAccessToken(accessToken);
            if (ja == null || ja.getRefreshToken() == null || isBlank(ja.getRefreshToken().getTokenValue())) {
                log.debug("Refresh token has already been revoked");
                return true;
            }
            // remove refresh token only since the access token is not required to be stored in refresh mode,
            // and also it's not necessary to revoke an access token in refresh mode.
            jwtStore.removeRefreshToken(ja.getRefreshToken().getTokenValue());
        }
        else {
            jwtStore.removeAccessToken(accessToken);
        }
        return true;
    }

    @Override
    public boolean revokeAuthenticationByRefreshToken(String refreshToken) {
        if (isBlank(refreshToken)) {
            log.debug("refresh token is blank");
            return false;
        }
        if (!jwtConfig.isRefreshRevokeType()) {
            log.warn("The revoke type is setup as %s, which is not for revoking refresh token".formatted(jwtConfig.getRevokeType()));
            return false;
        }
        if (log.isDebugEnabled())
            log.debug("Revoke JWT authentication by refresh token: %s".formatted(abbreviateToken(refreshToken)));
        if (!jwtConfig.isRefreshRevokeType()) {
            return false;
        }
        try {
            jwtTokenProvider.validateToken(refreshToken);
        } catch (ExpiredJwtException e) {
            // keep handling if token is expired
            log.debug("User ID from expired token: %s".formatted(e.getClaims().getSubject()));
        } catch (Exception e) {
            return false;
        }
        // only remove refresh token as only works in `refresh` mode.
        jwtStore.removeRefreshToken(refreshToken);
        return true;
    }

    @Override
    public boolean isRevokedRefreshToken(String refreshToken) {
        if (!jwtConfig.isRefreshRevokeType()) {
            log.warn("The revoke type is setup as %s, which is not for revoking refresh token".formatted(jwtConfig.getRevokeType()));
            throw new RuntimeException("To refresh access token, setup revoke type to be 'refresh' first.");
        }
        if (isBlank(refreshToken)) {
            return true;
        }
//        log.debug("%d refresh tokens in memory".formatted(jwtRepository.count()));
        JwtAuthentication jwtAuthentication = null;
        try {
            jwtAuthentication = jwtStore.loadByRefreshToken(refreshToken);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return true;
        }
        return jwtAuthentication == null || jwtAuthentication.getRefreshToken() == null || isBlank(jwtAuthentication.getRefreshToken().getTokenValue());
    }

    @Override
    public boolean isRevokedAccessToken(String accessToken) {
        if (isBlank(accessToken)) {
            return true;
        }
        JwtAuthentication jwtAuthentication = null;
        try {
            jwtAuthentication = jwtStore.loadByAccessToken(accessToken);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            return true;
        }
        return jwtAuthentication == null || jwtAuthentication.getAccessToken() == null || isBlank(jwtAuthentication.getAccessToken().getTokenValue());
    }

    @Override
    public JwtAuthentication getJwtAuthentication(String accessToken) {
        return jwtStore.loadByAccessToken(accessToken);
    }
}
