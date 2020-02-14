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
import org.swiftboot.demo.command.AdminUserSigninCommand;
import org.swiftboot.demo.result.AdminUserSigninResult;
import org.swiftboot.demo.service.AdminPermissionService;
import org.swiftboot.demo.service.AdminUserService;
import org.swiftboot.web.result.HttpResponse;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @author swiftech
 */
@Api(tags = {"AdminUserAuth管理员认证"})
@Controller
@RequestMapping("/admin/user")
@ResponseBody
public class AdminAuthController {
    private Logger log = LoggerFactory.getLogger(AdminAuthController.class);

    @Resource
    private AdminUserService adminUserService;

    @Resource
    private AdminPermissionService adminPermissionService;

    @Resource
    private SwiftbootAuthConfigBean authConfigBean;


    @ApiOperation(notes = "Admin user signin", value = "Admin user signin")
    @RequestMapping(value = "signin", method = RequestMethod.POST)
    public HttpResponse<AdminUserSigninResult> adminUserSign(
            @RequestBody AdminUserSigninCommand command,
            HttpServletResponse response) {
        log.info("> /admin/user/signin");
        AdminUserSigninResult adminUserResult = adminUserService.adminUserSignin(command);
        Cookie cookie  = new Cookie(authConfigBean.getSession().getTokenKey(), adminUserResult.getToken());
        response.addCookie(cookie);
        return new HttpResponse<>(adminUserResult);
    }


}
