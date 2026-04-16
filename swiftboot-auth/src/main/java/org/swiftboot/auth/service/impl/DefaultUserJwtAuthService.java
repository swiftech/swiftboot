package org.swiftboot.auth.service.impl;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.common.auth.AuthenticationException;
import org.swiftboot.auth.config.AuthConfigBean;
import org.swiftboot.auth.model.UserPersistable;
import org.swiftboot.auth.repository.UserAuthRepository;
import org.swiftboot.auth.service.UserAuthService;
import org.swiftboot.common.auth.JwtService;
import org.swiftboot.common.auth.JwtTokenProvider;
import org.swiftboot.common.auth.config.JwtConfigBean;
import org.swiftboot.common.auth.response.LogoutResponse;
import org.swiftboot.common.auth.token.AccessToken;
import org.swiftboot.common.auth.token.JwtAuthentication;
import org.swiftboot.common.auth.token.RefreshToken;
import org.swiftboot.util.PasswordUtils;
import org.swiftboot.web.i18n.MessageHelper;

import java.util.Map;
import java.util.Optional;

/**
 * @param <E> Type of user entity inherited from {@link UserPersistable}.
 * @since 3.0
 */
public class DefaultUserJwtAuthService<E extends UserPersistable> implements UserAuthService<JwtAuthentication> {

    private static final Logger log = LoggerFactory.getLogger(DefaultUserJwtAuthService.class);

    @Resource
    protected UserAuthRepository<E> userAuthRepository;

    @Resource
    protected JwtTokenProvider jwtTokenProvider;

    @Resource
    protected JwtService jwtService;

    @Resource
    protected AuthConfigBean authConfig;

    @Resource
    protected JwtConfigBean jwtConfig;

    @Resource
    protected MessageHelper messageHelper;

    @Override
    public JwtAuthentication userSignIn(String loginId, String loginPwd) {
        return this.userSignIn(loginId, loginPwd, null);
    }

    @Override
    public JwtAuthentication userSignIn(String loginId, String loginPwd, Map<String, Object> additions) {
        String encryptedPwd = PasswordUtils.createPassword(loginPwd, authConfig.getPasswordSalt());
        Optional<E> optUser = userAuthRepository.findByLoginNameAndLoginPwd(loginId, encryptedPwd);
        if (optUser.isPresent()) {
            E userEntity = optUser.get();
            log.debug("Sign in user id: %s".formatted(userEntity.getId()));
            return this.generateTokens(userEntity, additions, true);
        }
        else {
            log.warn("Sign in failed for user: %s".formatted(loginId));
            throw new AuthenticationException("Sign in failed");
        }
    }

    @Override
    public JwtAuthentication refreshAccessToken(String refreshToken) {
        return this.refreshAccessToken(refreshToken, null);
    }

    @Override
    public JwtAuthentication refreshAccessToken(String refreshToken, Map<String, Object> additions) {
        // validate refresh token
        try {
            if (StringUtils.isBlank(refreshToken) || !jwtTokenProvider.validateToken(refreshToken)) {
                throw new AuthenticationException(messageHelper.getMessage("swiftboot.auth.refresh.token.invalid"));
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage(), e);
            throw new AuthenticationException(messageHelper.getMessage("swiftboot.auth.refresh.token.invalid"));
        }
        // whether revoked.
        if (jwtService.isRevokedRefreshToken(refreshToken)) {
            throw new AuthenticationException(messageHelper.getMessage("swiftboot.auth.refresh.token.revoked"));
        }

        String userId = jwtTokenProvider.getUserId(refreshToken);
        Optional<E> optUserAuth = userAuthRepository.findById(userId);
        if (optUserAuth.isPresent()) {
            E userEntity = optUserAuth.get();

            // rolling means re-generate Refresh Token each time.
            boolean rolling = "rolling".equals(jwtConfig.getRefreshMode());
            // generate new access token and refresh token
            JwtAuthentication jwtAuthentication = this.generateTokens(userEntity, additions, rolling);

            if (rolling) {
                log.debug("[Rolling] Refreshing access token with new refresh token.");
                // revoke used refresh token before saving new authentication.
                jwtService.revokeAuthenticationByRefreshToken(refreshToken);
                // save new refresh token
                jwtService.saveJwtAuthentication(jwtAuthentication);
            }

            return jwtAuthentication;
        }
        else {
            log.warn("Could not find user for the refresh token: %s".formatted(userId));
            throw new AuthenticationException(messageHelper.getMessage("swiftboot.auth.refresh.token.user.not.found"));
        }
    }

    @Override
    public LogoutResponse<String> userLogout(String accessToken) {
        return new LogoutResponse<>(accessToken);
    }

    /**
     * Generate token(s) for user.
     *
     * @param userEntity
     * @param additions
     * @param withRefreshToken true to generate new refresh token.
     * @return
     */
    protected JwtAuthentication generateTokens(E userEntity, Map<String, Object> additions, boolean withRefreshToken) {
        try {
            AccessToken accessToken = jwtTokenProvider.generateAccessToken(userEntity.getId(), userEntity.getLoginName(), additions);
            if (withRefreshToken) {
                RefreshToken refreshToken = jwtTokenProvider.generateRefreshToken(userEntity.getId());
                return new JwtAuthentication(accessToken, refreshToken);
            }
            else {
                return new JwtAuthentication(accessToken, null);
            }
        } catch (Exception e) {
            log.error("Generating access token and refresh token failed, check whether the JWT secret is well setup.", e);
            throw new RuntimeException(e);
        }
    }

}
