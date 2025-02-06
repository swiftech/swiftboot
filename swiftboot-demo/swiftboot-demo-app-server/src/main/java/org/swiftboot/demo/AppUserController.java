package org.swiftboot.demo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.auth.controller.AuthenticatedResponse;
import org.swiftboot.auth.service.UserAuthService;
import org.swiftboot.web.result.HttpResponse;

import jakarta.annotation.Resource;

/**
 * App 用户认证接口
 *
 * @author swiftech 2020-02-05
 **/
@Api(tags = {"AppUser"})
@Controller
@RequestMapping("/app/user")
@ResponseBody
public class AppUserController {

    private static final Logger log = LoggerFactory.getLogger(AppUserController.class);

    @Resource
    private UserAuthService userAuthService;

    @ApiOperation(notes = "App user sign in", value = "App user sign in")
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
