package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.swiftboot.auth.service.UserAuthService;
import org.swiftboot.demo.command.AppUserSigninCommand;
import org.swiftboot.demo.command.RefreshTokenCommand;
import org.swiftboot.demo.dto.AppUserSignInDto;
import org.swiftboot.web.result.HttpResponse;

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
    private UserAuthService userAuthService;

    @Operation(description = "App user sign in")
    @PostMapping(value = "signin")
    public HttpResponse<AppUserSignInDto> appUserSign(
            @RequestBody AppUserSigninCommand command) {
        log.info("> /app/signin");
        return userAuthService.userSignIn(command.getLoginName(), command.getLoginPwd());
    }

    @Operation(description = "Refresh Access Token, used only for JWT mode")
    @PostMapping(value = "refresh_token")
    public HttpResponse<AppUserSignInDto> refreshToken(
            @RequestBody RefreshTokenCommand command) {
        log.info("> /app/refresh_token");
        return userAuthService.refreshAccessToken(command.getRefreshToken());
    }

}
