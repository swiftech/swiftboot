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

import java.util.Optional;

/**
 * @param <T> Type of user entity.
 * @since 3.0
 */
public class DefaultUserJwtAuthService<T extends UserPersistable> implements UserAuthService<JwtAuthentication> {

    private static final Logger log = LoggerFactory.getLogger(DefaultUserJwtAuthService.class);

    @Resource
    private UserAuthRepository<T> appUserRepository;

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    @Resource
    private JwtService jwtService;

    @Resource
    private AuthConfigBean authConfig;

    @Override
    public JwtAuthentication userSignIn(String loginId, String loginPwd) {
        String encryptedPwd = PasswordUtils.createPassword(loginPwd, authConfig.getPasswordSalt());
        Optional<T> optUser = appUserRepository.findByLoginNameAndLoginPwd(loginId, encryptedPwd);
        if (optUser.isPresent()) {
            T appUserEntity = optUser.get();
            log.debug(appUserEntity.getId());
            return this.generateTokens(appUserEntity);
        }
        else {
            log.warn("Sign in failed for user: %s".formatted(loginId));
            throw new ErrMessageException(ResponseCode.CODE_SIGNIN_FAIL);
        }
    }

    @Override
    public JwtAuthentication refreshAccessToken(String refreshToken) {
        // whether valid.
        if (StringUtils.isBlank(refreshToken) || !jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Refresh Token is invalid");
        }
        // whether revoked.
        if (jwtService.isRevokedRefreshToken(refreshToken)) {
            throw new RuntimeException("Refresh Token is revoked");
        }

        String userId = jwtTokenProvider.getUserId(refreshToken);
        Optional<T> byId = appUserRepository.findById(userId);
        if (byId.isPresent()) {
            T appUserEntity = byId.get();

            // generate new access token and refresh token
            JwtAuthentication jwtAuthentication = this.generateTokens(appUserEntity);

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
        LogoutResponse<String> response = new LogoutResponse(accessToken);
        return response;
    }

    private JwtAuthentication generateTokens(T appUserEntity) {
        AccessToken accessToken = jwtTokenProvider.generateAccessToken(appUserEntity.getId(), appUserEntity.getLoginName());
        RefreshToken refreshToken = jwtTokenProvider.generateRefreshToken(appUserEntity.getId());
        return new JwtAuthentication(accessToken, refreshToken);
    }

}
