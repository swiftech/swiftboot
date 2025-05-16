package org.swiftboot.auth.service.impl;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.auth.config.AuthConfigBean;
import org.swiftboot.auth.model.UserPersistable;
import org.swiftboot.auth.repository.UserAuthRepository;
import org.swiftboot.auth.service.UserAuthService;
import org.swiftboot.common.auth.JwtService;
import org.swiftboot.common.auth.JwtTokenProvider;
import org.swiftboot.common.auth.response.LogoutResponse;
import org.swiftboot.common.auth.token.AccessToken;
import org.swiftboot.common.auth.token.JwtAuthentication;
import org.swiftboot.common.auth.token.RefreshToken;
import org.swiftboot.util.PasswordUtils;
import org.swiftboot.web.exception.ErrMessageException;
import org.swiftboot.web.response.ResponseCode;

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
            return this.generateTokens(userEntity, additions);
        }
        else {
            log.warn("Sign in failed for user: %s".formatted(loginId));
            throw new ErrMessageException(ResponseCode.CODE_SIGNIN_FAIL);
        }
    }

    @Override
    public JwtAuthentication refreshAccessToken(String refreshToken) {
        // validate refresh token
        if (StringUtils.isBlank(refreshToken) || !jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Refresh Token is invalid");
        }
        // whether revoked.
        if (jwtService.isRevokedRefreshToken(refreshToken)) {
            throw new RuntimeException("Refresh Token is revoked");
        }

        String userId = jwtTokenProvider.getUserId(refreshToken);
        Optional<E> byId = userAuthRepository.findById(userId);
        if (byId.isPresent()) {
            E userEntity = byId.get();

            // generate new access token and refresh token
            JwtAuthentication jwtAuthentication = this.generateTokens(userEntity, null);

            // save new refresh token
            jwtService.saveJwtAuthentication(jwtAuthentication);

            // revoke used refresh token
            jwtService.revokeAuthenticationByRefreshToken(refreshToken);

            return jwtAuthentication;
        }
        else {
            log.warn("Refresh token failed for user: %s".formatted(userId));
            throw new ErrMessageException(ResponseCode.CODE_SIGNIN_FAIL);
        }
    }

    @Override
    public LogoutResponse<String> userLogout(String accessToken) {
        return new LogoutResponse<>(accessToken);
    }

    protected JwtAuthentication generateTokens(E userEntity, Map<String, Object> additions) {
        AccessToken accessToken = jwtTokenProvider.generateAccessToken(userEntity.getId(), userEntity.getLoginName(), additions);
        RefreshToken refreshToken = jwtTokenProvider.generateRefreshToken(userEntity.getId());
        return new JwtAuthentication(accessToken, refreshToken);
    }

}
