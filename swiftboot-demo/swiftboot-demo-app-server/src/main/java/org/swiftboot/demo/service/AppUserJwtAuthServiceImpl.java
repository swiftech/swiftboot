package org.swiftboot.demo.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.auth.model.JwtAuthentication;
import org.swiftboot.auth.service.*;
import org.swiftboot.common.auth.JwtTokenProvider;
import org.swiftboot.demo.dto.AppUserSignInDto;
import org.swiftboot.demo.repository.AppUserRepository;
import org.swiftboot.demo.model.AppUserEntity;
import org.swiftboot.util.CryptoUtils;
import org.swiftboot.util.PasswordUtils;
import org.swiftboot.util.time.LocalDateTimeUtils;
import org.swiftboot.web.exception.ErrMessageException;
import org.swiftboot.web.exception.ErrorCodeSupport;

import java.util.Optional;

/**
 * since 3.0.0
 */
public class AppUserJwtAuthServiceImpl implements UserAuthService {

    private static final Logger log = LoggerFactory.getLogger(AppUserJwtAuthServiceImpl.class);

    @Resource
    private AppUserRepository appUserRepository;

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    @PostConstruct
    public void initData() {
        // create a new user for testing
        Optional<AppUserEntity> optUser = appUserRepository.findByLoginName("13866669999");
        if (optUser.isEmpty()) {
            AppUserEntity newEntity = new AppUserEntity();
            newEntity.setLoginName("13866669999");
            newEntity.setLoginPwd(PasswordUtils.createPassword("12345678"));
            appUserRepository.save(newEntity);
        }
    }

    @Override
    public AuthenticatedResponse<AppUserSignInDto, JwtAuthentication> userSignIn(String loginId, String loginPwd) {
        String encryptedPwd = CryptoUtils.md5(loginPwd);
        Optional<AppUserEntity> optUser = appUserRepository.findByLoginNameAndLoginPwd(loginId, encryptedPwd);
        if (optUser.isPresent()) {
            AppUserEntity appUserEntity = optUser.get();
            log.debug(appUserEntity.getId());
            return generateTokensAndFillResponse(appUserEntity);
        }
        else {
            log.debug("Sign in failed for user: %s".formatted(loginId));
            throw new ErrMessageException(ErrorCodeSupport.CODE_SIGNIN_FAIL);
        }
    }

    @Override
    public AuthenticatedResponse<AppUserSignInDto, JwtAuthentication> refreshAccessToken(String refreshToken) {
        if (StringUtils.isBlank(refreshToken) || !jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Refresh Token is invalid");
        }
        String userId = jwtTokenProvider.getUserId(refreshToken);
        Optional<AppUserEntity> byId = appUserRepository.findById(userId);
        if (byId.isPresent()) {
            return this.generateTokensAndFillResponse(byId.get());
        }
        else {
            log.debug("Refresh token failed for user: %s".formatted(userId));
            throw new ErrMessageException(ErrorCodeSupport.CODE_SIGNIN_FAIL);
        }
    }


    private AuthenticatedResponse<AppUserSignInDto, JwtAuthentication> generateTokensAndFillResponse(AppUserEntity appUserEntity) {
        String accessToken = jwtTokenProvider.generateAccessToken(appUserEntity.getId(), appUserEntity.getLoginName());
        String refreshToken = jwtTokenProvider.generateRefreshToken(appUserEntity.getId());

        AppUserSignInDto dto = new AppUserSignInDto();
        dto.setId(appUserEntity.getId());
        dto.setLoginName(appUserEntity.getLoginName());
        dto.setUpdateTime(LocalDateTimeUtils.toMillisecond(appUserEntity.getUpdateTime()));
        dto.setAccessToken(accessToken);
        dto.setExpiresAt(jwtTokenProvider.getExpireTime(accessToken).getTime());
        dto.setRefreshToken(refreshToken);
        dto.setRefreshTokenExpiresAt(jwtTokenProvider.getExpireTime(refreshToken).getTime());
        JwtAuthentication jwtAuthentication = new JwtAuthentication(accessToken, dto.getExpiresAt(), refreshToken, dto.getRefreshTokenExpiresAt());
        return new AuthenticatedResponse<>(dto, jwtAuthentication);
    }

}
