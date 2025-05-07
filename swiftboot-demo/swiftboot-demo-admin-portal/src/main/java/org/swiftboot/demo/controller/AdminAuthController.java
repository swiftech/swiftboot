package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.demo.dto.AdminUserSigninResult;
import org.swiftboot.demo.dto.AdminUserSignoutResult;
import org.swiftboot.demo.request.AdminUserSigninRequest;
import org.swiftboot.demo.request.AdminUserSignoutRequest;
import org.swiftboot.demo.service.AdminPermissionService;
import org.swiftboot.demo.service.AdminUserService;
import org.swiftboot.shiro.config.SwiftbootShiroConfigBean;
import org.swiftboot.web.response.Response;

/**
 * @author swiftech
 */
@Tag(name = "AdminUserAuth管理员认证")
@Controller
@RequestMapping("/admin/auth")
@ResponseBody
public class AdminAuthController {
    private static final Logger log = LoggerFactory.getLogger(AdminAuthController.class);

    @Resource
    private AdminUserService adminUserService;

    @Resource
    private AdminPermissionService adminPermissionService;

    @Resource
    private SwiftbootShiroConfigBean shiroConfigBean;


    @Operation(description = "Admin user signin")
    @RequestMapping(value = "signin", method = RequestMethod.POST)
    public Response<AdminUserSigninResult> adminUserSignin(
            @RequestBody AdminUserSigninRequest request,
            HttpServletResponse response) {
        log.info("> /admin/auth/signin");
        AdminUserSigninResult adminUserResult = adminUserService.adminUserSignin(request);
        String tokenKey = shiroConfigBean.getCookie().getName();
        Cookie cookie  = new Cookie(tokenKey, adminUserResult.getToken());
        cookie.setDomain(shiroConfigBean.getCookie().getDomain());
        cookie.setMaxAge(shiroConfigBean.getCookie().getMaxAge());
        response.addCookie(cookie);
        return new Response<>(adminUserResult);
    }

    @Operation(description = "Admin user signout")
    @RequestMapping(value = "signout", method = RequestMethod.POST)
    public Response<AdminUserSignoutResult> adminUserSignout(
            @RequestBody AdminUserSignoutRequest request,
            HttpServletResponse response) {
        log.info("> /admin/auth/signout");
        AdminUserSignoutResult adminUserResult = adminUserService.adminUserSignout(request);
        String tokenKey = shiroConfigBean.getCookie().getName();
        Cookie cookie  = new Cookie(tokenKey, null);
        cookie.setDomain(shiroConfigBean.getCookie().getDomain());
        cookie.setMaxAge(shiroConfigBean.getCookie().getMaxAge());
        response.addCookie(cookie);
        return new Response<>(adminUserResult);
    }
}
