package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.auth.service.UserAuthService;
import org.swiftboot.web.exception.ErrorCodeSupport;
import org.swiftboot.web.result.HttpResponse;
import org.swiftboot.common.auth.annotation.Token;

/**
 * @since 3.0
 */
@Tag(name = "App Logout")
@Controller
@RequestMapping("/app")
@ResponseBody
public class AppLogoutController {

    private static final Logger log = LoggerFactory.getLogger(AppLogoutController.class);

    @Resource
    private UserAuthService userAuthService;

    @Operation(description = "App user logout")
    @PostMapping(value = "logout")
    public HttpResponse<Void> appUserLogout(@Token String accessToken) {
        log.info("> /app/logout");
        userAuthService.userLogout(accessToken);
        return new HttpResponse<>(ErrorCodeSupport.CODE_OK, "Logout success");
    }

}
