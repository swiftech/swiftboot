package org.swiftboot.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.auth.SwiftbootAuthConfigBean;
import org.swiftboot.demo.command.AppUserSigninCommand;
import org.swiftboot.demo.result.AppUserSigninResult;
import org.swiftboot.demo.service.AppUserService;
import org.swiftboot.web.result.HttpResponse;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * 管理员
 *
 * @author swiftech 2020-02-05
 **/
@Api(tags = {"AppUser"})
@Controller
@RequestMapping("/app/user")
@ResponseBody
public class AppUserController {

    private final Logger log = LoggerFactory.getLogger(AppUserController.class);

    @Resource
    private AppUserService appUserService;

    @Resource
    private SwiftbootAuthConfigBean authConfigBean;

    @ApiOperation(notes = "App user signin", value = "App user signin")
    @RequestMapping(value = "signin", method = RequestMethod.POST)
    public HttpResponse<AppUserSigninResult> appUserSign(
            @RequestBody AppUserSigninCommand command,
            HttpServletResponse response) {
        log.info("> /app/user/signin");
        AppUserSigninResult appUserSigninResult = appUserService.appUserSignin(command);
        Cookie cookie = new Cookie(authConfigBean.getSession().getTokenKey(), appUserSigninResult.getToken());
        cookie.setPath("/");
        response.addCookie(cookie);
        return new HttpResponse<>(appUserSigninResult);
    }


}
