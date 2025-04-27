package org.swiftboot.demo.service;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.common.auth.response.AuthenticatedResponse;
import org.swiftboot.auth.service.UserAuthService;
import org.swiftboot.common.auth.JwtService;
import org.swiftboot.common.auth.JwtTokenProvider;
import org.swiftboot.common.auth.response.LogoutResponse;
import org.swiftboot.common.auth.token.AccessToken;
import org.swiftboot.common.auth.token.JwtAuthentication;
import org.swiftboot.common.auth.token.RefreshToken;
import org.swiftboot.demo.dto.AppUserSignInDto;
import org.swiftboot.demo.model.AppUserEntity;
import org.swiftboot.demo.repository.AppUserRepository;
import org.swiftboot.util.CryptoUtils;
import org.swiftboot.util.time.LocalDateTimeUtils;
import org.swiftboot.web.exception.ErrMessageException;
import org.swiftboot.web.response.ResponseCode;

import java.util.Optional;

/**
 * since 3.0.0
 */
public class AppUserJwtAuthService implements UserAuthService {

    private static final Logger log = LoggerFactory.getLogger(AppUserJwtAuthService.class);

    @Resource
    private AppUserRepository appUserRepository;

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    @Resource
    private JwtService jwtService;

    @Override
    public AuthenticatedResponse<AppUserSignInDto, JwtAuthentication> userSignIn(String loginId, String loginPwd) {
        String encryptedPwd = CryptoUtils.md5(loginPwd);
        Optional<AppUserEntity> optUser = appUserRepository.findByLoginNameAndLoginPwd(loginId, encryptedPwd);
        if (optUser.isPresent()) {
            AppUserEntity appUserEntity = optUser.get();
            log.debug(appUserEntity.getId());
            JwtAuthentication jwtAuthentication = this.generateTokens(appUserEntity);
            return this.createResponse(appUserEntity, jwtAuthentication);
        }
        else {
            log.debug("Sign in failed for user: %s".formatted(loginId));
            throw new ErrMessageException(ResponseCode.CODE_SIGNIN_FAIL);
        }
    }

    @Override
    public AuthenticatedResponse<AppUserSignInDto, JwtAuthentication> refreshAccessToken(String refreshToken) {
        // whether valid.
        if (StringUtils.isBlank(refreshToken) || !jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Refresh Token is invalid");
        }
        // whether revoked.
        if (jwtService.isRevokedRefreshToken(refreshToken)) {
            throw new RuntimeException("Refresh Token is revoked");
        }

        String userId = jwtTokenProvider.getUserId(refreshToken);
        Optional<AppUserEntity> byId = appUserRepository.findById(userId);
        if (byId.isPresent()) {
            AppUserEntity appUserEntity = byId.get();

            // generate new access token and refresh token
            JwtAuthentication jwtAuthentication = this.generateTokens(appUserEntity);

            // save new refresh token
            jwtService.saveJwtAuthentication(jwtAuthentication);

            // revoke used refresh token
            jwtService.revokeAuthenticationByRefreshToken(refreshToken);

            return this.createResponse(appUserEntity, jwtAuthentication);
        }
        else {
            log.debug("Refresh token failed for user: %s".formatted(userId));
            throw new ErrMessageException(ResponseCode.CODE_SIGNIN_FAIL);
        }
    }

    @Override
    public LogoutResponse<String> userLogout(String accessToken) {
        LogoutResponse<String> response = new LogoutResponse(accessToken);
        return response;
    }

    private JwtAuthentication generateTokens(AppUserEntity appUserEntity) {
        AccessToken accessToken = jwtTokenProvider.generateAccessToken(appUserEntity.getId(), appUserEntity.getLoginName());
        RefreshToken refreshToken = jwtTokenProvider.generateRefreshToken(appUserEntity.getId());
        return new JwtAuthentication(accessToken, refreshToken);
    }

    private AuthenticatedResponse<AppUserSignInDto, JwtAuthentication> createResponse(AppUserEntity appUserEntity, JwtAuthentication jwtAuthentication) {
        AccessToken accessToken = jwtAuthentication.getAccessToken();
        RefreshToken refreshToken = jwtAuthentication.getRefreshToken();
        AppUserSignInDto dto = new AppUserSignInDto();
        dto.setId(appUserEntity.getId());
        dto.setLoginName(appUserEntity.getLoginName());
        dto.setUpdateTime(LocalDateTimeUtils.toMillisecond(appUserEntity.getUpdateTime()));
        dto.setAccessToken(accessToken.tokenValue());
        dto.setExpiresAt(accessToken.expiresAt());
        dto.setRefreshToken(refreshToken.tokenValue());
        dto.setRefreshTokenExpiresAt(refreshToken.expiresAt());
        return new AuthenticatedResponse<>(dto, jwtAuthentication);
    }
}
