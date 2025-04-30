package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.auth.model.Session;
import org.swiftboot.auth.service.UserAuthService;
import org.swiftboot.common.auth.annotation.UserId;
import org.swiftboot.common.auth.request.NamePasswordLoginRequest;
import org.swiftboot.common.auth.request.RefreshTokenRequest;
import org.swiftboot.common.auth.response.AuthenticatedResponse;
import org.swiftboot.common.auth.token.AccessToken;
import org.swiftboot.common.auth.token.Authenticated;
import org.swiftboot.common.auth.token.JwtAuthentication;
import org.swiftboot.common.auth.token.RefreshToken;
import org.swiftboot.demo.dto.AppUserSignInDto;
import org.swiftboot.demo.model.AppUserEntity;
import org.swiftboot.demo.repository.AppUserRepository;
import org.swiftboot.util.time.LocalDateTimeUtils;
import org.swiftboot.web.exception.ErrMessageException;
import org.swiftboot.web.response.Response;

import java.util.Optional;

/**
 * App 用户认证接口
 *
 * @author swiftech 2020-02-05
 **/
@Tag(name = "App Login")
@Controller
@RequestMapping("/app")
@ResponseBody
public class AppLoginController {

    private static final Logger log = LoggerFactory.getLogger(AppLoginController.class);

    @Resource
    private UserAuthService<?> userAuthService;

    @Resource
    private AppUserRepository appUserRepository;

    @Operation(description = "App user sign in, return access token and refresh token if in JWT mode; return in HTTP header or set cookie(if enabled) if in Session mode")
    @PostMapping(value = "signin")
    public Response<AppUserSignInDto> appUserSign(
            @RequestBody NamePasswordLoginRequest command) {
        log.info("> /app/signin");
        Authenticated authenticated = userAuthService.userSignIn(command.getLoginName(), command.getLoginPwd());
        Optional<AppUserEntity> opt = appUserRepository.findByLoginName(command.getLoginName());
        if (authenticated instanceof JwtAuthentication jwta) {
            return this.createResponse(opt.get(), jwta);
        }
        else if (authenticated instanceof Session session) {
            return this.createSessionResponse(opt.get(), session);
        }
        else {
            throw new ErrMessageException("No user found");
        }
    }

    @Operation(description = "Refresh Access Token, used only for JWT mode")
    @PostMapping(value = "refresh_token")
    public Response<AppUserSignInDto> refreshToken(
            @RequestBody RefreshTokenRequest command, @UserId String userId) {
        log.info("> /app/refresh_token");
        Authenticated authenticated = userAuthService.refreshAccessToken(command.getRefreshToken());
        Optional<AppUserEntity> opt = appUserRepository.findById(userId);
        if (authenticated instanceof JwtAuthentication jwta) {
            return this.createResponse(opt.get(), jwta);
        }
        else if (authenticated instanceof Session session) {
            return this.createSessionResponse(opt.get(), session);
        }
        else {
            throw new ErrMessageException("No user found");
        }
    }

    private AuthenticatedResponse<AppUserSignInDto, Session> createSessionResponse(AppUserEntity appUserEntity, Session session) {
        AppUserSignInDto signInDto = new AppUserSignInDto();
        signInDto.setAuthType("session");
        signInDto.setId(appUserEntity.getId());
        signInDto.setLoginName(appUserEntity.getLoginName());
        signInDto.setAccessToken(session.getUserToken());
        signInDto.setExpiresAt(session.getExpireTime());
        return new AuthenticatedResponse(signInDto, session);
    }

    private AuthenticatedResponse<AppUserSignInDto, JwtAuthentication> createResponse(AppUserEntity appUserEntity, JwtAuthentication jwtAuthentication) {
        AccessToken accessToken = jwtAuthentication.getAccessToken();
        RefreshToken refreshToken = jwtAuthentication.getRefreshToken();
        AppUserSignInDto dto = new AppUserSignInDto();
        dto.setAuthType("jwt");
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
