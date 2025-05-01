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
import org.swiftboot.common.auth.annotation.Token;
import org.swiftboot.common.auth.response.LogoutResponse;
import org.swiftboot.common.auth.token.JwtAuthentication;
import org.swiftboot.web.response.Response;

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
    private UserAuthService<JwtAuthentication> userAuthService;

    @Operation(description = "App user logout")
    @PostMapping(value = "logout")
    public Response<String> appUserLogout(@Token String accessToken) {
        log.info("> /app/logout");
        LogoutResponse<String> objectLogoutResponse = userAuthService.userLogout(accessToken);
        objectLogoutResponse.setMessage("Logout success");
        return objectLogoutResponse;
    }

}
