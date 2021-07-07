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
import org.swiftboot.demo.command.AdminUserSigninCommand;
import org.swiftboot.demo.command.AdminUserSignoutCommand;
import org.swiftboot.demo.result.AdminUserSigninResult;
import org.swiftboot.demo.result.AdminUserSignoutResult;
import org.swiftboot.demo.service.AdminPermissionService;
import org.swiftboot.demo.service.AdminUserService;
import org.swiftboot.shiro.SwiftbootShiroConfigBean;
import org.swiftboot.web.result.HttpResponse;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author swiftech
 */
@Api(tags = {"AdminUserAuth管理员认证"})
@Controller
@RequestMapping("/admin/auth")
@ResponseBody
public class AdminAuthController {
    private final Logger log = LoggerFactory.getLogger(AdminAuthController.class);

    @Resource
    private AdminUserService adminUserService;

    @Resource
    private AdminPermissionService adminPermissionService;

    @Resource
    private SwiftbootShiroConfigBean shiroConfigBean;


    @ApiOperation(notes = "Admin user signin", value = "Admin user signin")
    @RequestMapping(value = "signin", method = RequestMethod.POST)
    public HttpResponse<AdminUserSigninResult> adminUserSignin(
            @RequestBody AdminUserSigninCommand command,
            HttpServletResponse response) {
        log.info("> /admin/auth/signin");
        AdminUserSigninResult adminUserResult = adminUserService.adminUserSignin(command);
        String tokenKey = shiroConfigBean.getCookie().getName();
        Cookie cookie  = new Cookie(tokenKey, adminUserResult.getToken());
        cookie.setDomain(shiroConfigBean.getCookie().getDomain());
        cookie.setMaxAge(shiroConfigBean.getCookie().getMaxAge());
        response.addCookie(cookie);
        return new HttpResponse<>(adminUserResult);
    }

    @ApiOperation(notes = "Admin user signout", value = "Admin user signout")
    @RequestMapping(value = "signout", method = RequestMethod.POST)
    public HttpResponse<AdminUserSignoutResult> adminUserSignout(
            @RequestBody AdminUserSignoutCommand command,
            HttpServletResponse response) {
        log.info("> /admin/auth/signout");
        AdminUserSignoutResult adminUserResult = adminUserService.adminUserSignout(command);
        String tokenKey = shiroConfigBean.getCookie().getName();
        Cookie cookie  = new Cookie(tokenKey, null);
        cookie.setDomain(shiroConfigBean.getCookie().getDomain());
        cookie.setMaxAge(shiroConfigBean.getCookie().getMaxAge());
        response.addCookie(cookie);
        return new HttpResponse<>(adminUserResult);
    }
}
