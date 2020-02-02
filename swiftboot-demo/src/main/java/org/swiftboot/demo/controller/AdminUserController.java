package org.swiftboot.demo.controller;

import org.swiftboot.auth.SwiftbootAuthConfigBean;
import org.swiftboot.demo.command.AdminUserCreateCommand;
import org.swiftboot.demo.command.AdminUserSaveCommand;
import org.swiftboot.demo.command.AdminUserSigninCommand;
import org.swiftboot.demo.result.*;
import org.swiftboot.demo.service.AdminUserService;
import org.swiftboot.util.JsonUtils;
import org.swiftboot.web.result.HttpResponse;
import org.swiftboot.web.command.IdCommand;
import org.swiftboot.web.command.IdListCommand;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * 管理员
 *
 * @author swiftech 2020-01-06
 **/
@Api(tags = {"AdminUser管理员"})
@Controller
@RequestMapping("/admin/user")
@ResponseBody
public class AdminUserController {

    private Logger log = LoggerFactory.getLogger(AdminUserController.class);

    @Resource
    private AdminUserService adminUserService;

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

    @ApiOperation(notes = "创建管理员", value = "创建管理员")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public HttpResponse<AdminUserCreateResult> adminUserCreate(
            @RequestBody @Validated @ApiParam("创建管理员参数") AdminUserCreateCommand command) {
        log.info("> /admin/user/create");
        log.debug(JsonUtils.object2PrettyJson(command));
        AdminUserCreateResult ret = adminUserService.createAdminUser(command);
        return new HttpResponse<>(ret);
    }

    @ApiOperation(notes = "保存管理员", value = "保存管理员")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public HttpResponse<AdminUserSaveResult> adminUserSave(
            @RequestBody @Validated @ApiParam("保存管理员参数") AdminUserSaveCommand command) {
        log.info("> /admin/user/save");
        log.debug(JsonUtils.object2PrettyJson(command));
        AdminUserSaveResult ret = adminUserService.saveAdminUser(command);
        return new HttpResponse<>(ret);
    }

    @ApiOperation(notes = "查询管理员", value = "查询管理员")
    @RequestMapping(value = "query", method = RequestMethod.GET)
    public HttpResponse<AdminUserResult> adminUserQuery(
            @RequestParam("admin_user_id") String adminUserId) {
        log.info("> /admin/user/query");
        log.debug("  admin_user_id" + adminUserId);
        AdminUserResult adminUserResult = adminUserService.queryAdminUser(adminUserId);
        return new HttpResponse<>(adminUserResult);
    }

    @ApiOperation(notes = "查询管理员列表", value = "查询管理员列表")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public HttpResponse<AdminUserListResult> adminUserList() {
        log.info("> /admin/user/list");
        AdminUserListResult ret = adminUserService.queryAdminUserList();
        return new HttpResponse<>(ret);
    }

    @ApiOperation(notes = "逻辑删除管理员", value = "逻辑删除管理员")
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public HttpResponse<Void> adminUserDelete(
            @RequestBody @Validated @ApiParam("管理员ID") IdCommand command) {
        log.info("> /admin/user/delete");
        log.debug(JsonUtils.object2PrettyJson(command));
        adminUserService.deleteAdminUser(command.getId());
        return new HttpResponse<>();
    }

    @ApiOperation(notes = "逻辑删除多个管理员", value = "逻辑删除多个管理员")
    @RequestMapping(value = "delete/list", method = RequestMethod.DELETE)
    public HttpResponse<Void> adminUserDeleteList(
            @RequestBody @Validated @ApiParam("管理员ID列表") IdListCommand command) {
        log.info("> /admin/user/delete/list");
        log.debug(JsonUtils.object2PrettyJson(command));
        adminUserService.deleteAdminUserList(command);
        return new HttpResponse<>();
    }


    @ApiOperation(notes = "永久删除管理员", value = "永久删除管理员")
    @RequestMapping(value = "purge", method = RequestMethod.DELETE)
    public HttpResponse<Void> adminUserPurge(
            @RequestBody @Validated @ApiParam("管理员ID") IdCommand command) {
        log.info("> /admin/user/purge");
        log.debug(JsonUtils.object2PrettyJson(command));
        adminUserService.purgeAdminUser(command.getId());
        return new HttpResponse<>();
    }

    @ApiOperation(notes = "永久删除多个管理员", value = "永久删除多个管理员")
    @RequestMapping(value = "purge/list", method = RequestMethod.DELETE)
    public HttpResponse<Void> adminUserPurgeList(
            @RequestBody @Validated @ApiParam("管理员ID列表") IdListCommand command) {
        log.info("> /admin/user/purge/list");
        log.debug(JsonUtils.object2PrettyJson(command));
        adminUserService.purgeAdminUserList(command);
        return new HttpResponse<>();
    }

}
