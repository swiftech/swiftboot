package org.swiftboot.demo.controller;

import org.swiftboot.demo.result.AdminUserCreateResult;
import org.swiftboot.demo.result.AdminUserListResult;
import org.swiftboot.demo.result.AdminUserResult;
import org.swiftboot.demo.result.AdminUserSaveResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.swiftboot.auth.config.SwiftbootAuthConfigBean;
import org.swiftboot.demo.request.AdminUserCreateCommand;
import org.swiftboot.demo.request.AdminUserSaveCommand;
import org.swiftboot.demo.config.PermissionConfigBean;
import org.swiftboot.demo.service.AdminPermissionService;
import org.swiftboot.demo.service.AdminUserService;
import org.swiftboot.shiro.constant.ShiroSessionConstants;
import org.swiftboot.util.JsonUtils;
import org.swiftboot.web.request.IdCommand;
import org.swiftboot.web.request.IdListCommand;
import org.swiftboot.web.exception.ErrMessageException;
import org.swiftboot.web.exception.ErrorCodeSupport;
import org.swiftboot.web.dto.HttpResponse;

import jakarta.annotation.Resource;
import java.util.Set;

/**
 * 管理员
 *
 * @author swiftech 2020-01-06
 **/
@Tag(name = "AdminUser管理员"})
@Controller
@RequestMapping("/admin/user")
@ResponseBody
public class AdminUserController {

    private static final Logger log = LoggerFactory.getLogger(AdminUserController.class);

    @Resource
    private AdminUserService adminUserService;

    @Resource
    private AdminPermissionService adminPermissionService;

    @Resource
    private SwiftbootAuthConfigBean authConfigBean;


    @Operation(description = "创建管理员", value = "创建管理员")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public HttpResponse<AdminUserCreateResult> adminUserCreate(
            @RequestBody @Validated @Parameter(description = "创建管理员参数") AdminUserCreateCommand command) {
        log.info("> /admin/user/create");
        log.debug(JsonUtils.object2PrettyJson(command));
        AdminUserCreateResult ret = adminUserService.createAdminUser(command);
        return new HttpResponse<>(ret);
    }

    @Operation(description = "保存管理员", value = "保存管理员")
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public HttpResponse<AdminUserSaveResult> adminUserSave(
            @RequestBody @Validated @Parameter(description = "保存管理员参数") AdminUserSaveCommand command) {
        log.info("> /admin/user/save");
        log.debug(JsonUtils.object2PrettyJson(command));
        AdminUserSaveResult ret = adminUserService.saveAdminUser(command);
        return new HttpResponse<>(ret);
    }

    @Operation(description = "查询管理员", value = "查询管理员")
    @RequestMapping(value = "query", method = RequestMethod.GET)
    public HttpResponse<AdminUserResult> adminUserQuery(
            @RequestParam("admin_user_id") String adminUserId) {
        log.info("> /admin/user/query");
        log.debug("  admin_user_id" + adminUserId);
        AdminUserResult adminUserResult = adminUserService.queryAdminUser(adminUserId);
        return new HttpResponse<>(adminUserResult);
    }

    @Operation(description = "查询管理员列表", value = "查询管理员列表")
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public HttpResponse<AdminUserListResult> adminUserList() {
        log.info("> /admin/user/list");
        AdminUserListResult ret = adminUserService.queryAdminUserList();
        return new HttpResponse<>(ret);
    }

    @Operation(description = "查询管理员权限列表", value = "查询管理员权限列表")
    @RequestMapping(value = "permissions", method = RequestMethod.GET)
    public HttpResponse<Set<PermissionConfigBean>> adminUserPermissionList() {
        log.info("> /admin/user/permissions");
        Session session = SecurityUtils.getSubject().getSession();
        if (session == null) {
            throw new ErrMessageException(ErrorCodeSupport.CODE_USER_SESSION_NOT_EXIST);
        }
        if (session.getAttribute(ShiroSessionConstants.SESSION_KEY_LOGIN_NAME) == null) {
            throw new ErrMessageException(ErrorCodeSupport.CODE_SYS_ERR, "No login name found");
        }
        Set<PermissionConfigBean> ret = adminPermissionService.queryAllPermissionForUser(
                session.getAttribute(ShiroSessionConstants.SESSION_KEY_LOGIN_NAME).toString());
        return new HttpResponse<>(ret);
    }

    @Operation(description = "逻辑删除管理员", value = "逻辑删除管理员")
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public HttpResponse<Void> adminUserDelete(
            @RequestBody @Validated @Parameter(description = "管理员ID") IdCommand command) {
        log.info("> /admin/user/delete");
        log.debug(JsonUtils.object2PrettyJson(command));
        adminUserService.deleteAdminUser(command.getId());
        return new HttpResponse<>();
    }

    @Operation(description = "逻辑删除多个管理员", value = "逻辑删除多个管理员")
    @RequestMapping(value = "delete/list", method = RequestMethod.DELETE)
    public HttpResponse<Void> adminUserDeleteList(
            @RequestBody @Validated @Parameter(description = "管理员ID列表") IdListCommand command) {
        log.info("> /admin/user/delete/list");
        log.debug(JsonUtils.object2PrettyJson(command));
        adminUserService.deleteAdminUserList(command);
        return new HttpResponse<>();
    }


    @Operation(description = "永久删除管理员", value = "永久删除管理员")
    @RequestMapping(value = "purge", method = RequestMethod.DELETE)
    public HttpResponse<Void> adminUserPurge(
            @RequestBody @Validated @Parameter(description = "管理员ID") IdCommand command) {
        log.info("> /admin/user/purge");
        log.debug(JsonUtils.object2PrettyJson(command));
        adminUserService.purgeAdminUser(command.getId());
        return new HttpResponse<>();
    }

    @Operation(description = "永久删除多个管理员", value = "永久删除多个管理员")
    @RequestMapping(value = "purge/list", method = RequestMethod.DELETE)
    public HttpResponse<Void> adminUserPurgeList(
            @RequestBody @Validated @Parameter(description = "管理员ID列表") IdListCommand command) {
        log.info("> /admin/user/purge/list");
        log.debug(JsonUtils.object2PrettyJson(command));
        adminUserService.purgeAdminUserList(command);
        return new HttpResponse<>();
    }

}
