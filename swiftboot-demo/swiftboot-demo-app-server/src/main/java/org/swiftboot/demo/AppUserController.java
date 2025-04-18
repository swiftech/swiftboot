package org.swiftboot.demo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.auth.service.AuthenticatedResponse;
import org.swiftboot.auth.service.UserAuthService;
import org.swiftboot.web.result.HttpResponse;

/**
 * App 用户认证接口
 *
 * @author swiftech 2020-02-05
 **/
@Tag(name = "AppUser")
@Controller
@RequestMapping("/app/user")
@ResponseBody
public class AppUserController {

    private static final Logger log = LoggerFactory.getLogger(AppUserController.class);

    @Resource
    private UserAuthService userAuthService;

    @Operation(description = "App user sign in")
    @RequestMapping(value = "signin", method = RequestMethod.POST)
    public HttpResponse<AppUserSigninResult> appUserSign(
            @RequestBody AppUserSigninCommand command) {
        log.info("> /app/user/signin");
        AuthenticatedResponse<AppUserSigninResult> response = userAuthService.userSignIn(command.getLoginName(), command.getLoginPwd());
        AppUserSigninResult appUserSigninResult = new AppUserSigninResult();
        appUserSigninResult.setId(response.getUserSession().getUserId());
        appUserSigninResult.setLoginName(response.getUserSession().getUserName());
        appUserSigninResult.setAccessToken(response.getUserSession().getUserToken());
        response.setResult(appUserSigninResult);
        return response;
    }

}
