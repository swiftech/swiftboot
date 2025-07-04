package org.swiftboot.demo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.swiftboot.demo.config.PermissionConfigBean;
import org.swiftboot.demo.dto.AdminUserCreateResult;
import org.swiftboot.demo.dto.AdminUserListResult;
import org.swiftboot.demo.dto.AdminUserResult;
import org.swiftboot.demo.dto.AdminUserSaveResult;
import org.swiftboot.demo.request.AdminUserRequest;
import org.swiftboot.demo.service.AdminPermissionService;
import org.swiftboot.demo.service.AdminUserService;
import org.swiftboot.util.JsonUtils;
import org.swiftboot.web.request.IdListRequest;
import org.swiftboot.web.request.IdRequest;
import org.swiftboot.web.response.Response;

import java.util.HashSet;
import java.util.Set;

/**
 * 管理员
 *
 * @author swiftech 2020-01-06
 **/
@Tag(name = "AdminUser管理员")
@Controller
@RequestMapping("/admin/user")
@ResponseBody
public class AdminUserController {

    private static final Logger log = LoggerFactory.getLogger(AdminUserController.class);

    @Resource
    private AdminUserService adminUserService;

    @Resource
    private AdminPermissionService adminPermissionService;

//    @Resource
//    private SwiftbootAuthConfigBean authConfigBean;


    @Operation(description = "创建管理员")
    @PostMapping(value = "create")
    public Response<AdminUserCreateResult> adminUserCreate(
            @RequestBody @Validated @Parameter(description = "创建管理员参数") AdminUserRequest request) {
        log.info("> /admin/user/create");
        log.debug(JsonUtils.object2PrettyJson(request));
        AdminUserCreateResult ret = adminUserService.createAdminUser(request);
        return new Response<>(ret);
    }

    @Operation(description = "保存管理员")
    @PutMapping(value = "{userId}")
    public Response<AdminUserSaveResult> adminUserSave(@PathVariable String userId,
            @RequestBody @Validated @Parameter(description = "保存管理员参数") AdminUserRequest request) {
        log.info("> /admin/user/save");
        log.debug(JsonUtils.object2PrettyJson(request));
        AdminUserSaveResult ret = adminUserService.saveAdminUser(userId, request);
        return new Response<>(ret);
    }

    @Operation(description = "查询管理员")
    @GetMapping(value = "query")
    public Response<AdminUserResult> adminUserQuery(
            @RequestParam("admin_user_id") String adminUserId) {
        log.info("> /admin/user/query");
        log.debug("  admin_user_id" + adminUserId);
        AdminUserResult adminUserResult = adminUserService.queryAdminUser(adminUserId);
        return new Response<>(adminUserResult);
    }

    @Operation(description = "查询管理员列表")
    @GetMapping(value = "list")
    public Response<AdminUserListResult> adminUserList() {
        log.info("> /admin/user/list");
        AdminUserListResult ret = adminUserService.queryAdminUserList();
        return new Response<>(ret);
    }

    @Operation(description = "查询管理员权限列表")
    @GetMapping(value = "permissions")
    public Response<Set<PermissionConfigBean>> adminUserPermissionList() {
        log.info("> /admin/user/permissions");
//        Session session = SecurityUtils.getSubject().getSession();
//        if (session == null) {
//            throw new ErrMessageException(ResponseCode.CODE_USER_SESSION_NOT_EXIST);
//        }
//        if (session.getAttribute(ShiroSessionConstants.SESSION_KEY_LOGIN_NAME) == null) {
//            throw new ErrMessageException(ResponseCode.CODE_SYS_ERR, "No login name found");
//        }
//        Set<PermissionConfigBean> ret = adminPermissionService.queryAllPermissionForUser(
//                session.getAttribute(ShiroSessionConstants.SESSION_KEY_LOGIN_NAME).toString());
        Set<PermissionConfigBean> ret = new HashSet<>();
        return new Response<>(ret);
    }

    @Operation(description = "逻辑删除管理员")
    @DeleteMapping(value = "delete")
    public Response<Void> adminUserDelete(
            @RequestBody @Validated @Parameter(description = "管理员ID") IdRequest request) {
        log.info("> /admin/user/delete");
        log.debug(JsonUtils.object2PrettyJson(request));
        adminUserService.deleteAdminUser(request.getId());
        return new Response<>();
    }

    @Operation(description = "逻辑删除多个管理员")
    @DeleteMapping(value = "delete/list")
    public Response<Void> adminUserDeleteList(
            @RequestBody @Validated @Parameter(description = "管理员ID列表") IdListRequest request) {
        log.info("> /admin/user/delete/list");
        log.debug(JsonUtils.object2PrettyJson(request));
        adminUserService.deleteAdminUserList(request);
        return new Response<>();
    }


    @Operation(description = "永久删除管理员")
    @DeleteMapping(value = "purge")
    public Response<Void> adminUserPurge(
            @RequestBody @Validated @Parameter(description = "管理员ID") IdRequest request) {
        log.info("> /admin/user/purge");
        log.debug(JsonUtils.object2PrettyJson(request));
        adminUserService.purgeAdminUser(request.getId());
        return new Response<>();
    }

    @Operation(description = "永久删除多个管理员")
    @DeleteMapping(value = "purge/list")
    public Response<Void> adminUserPurgeList(
            @RequestBody @Validated @Parameter(description = "管理员ID列表") IdListRequest request) {
        log.info("> /admin/user/purge/list");
        log.debug(JsonUtils.object2PrettyJson(request));
        adminUserService.purgeAdminUserList(request);
        return new Response<>();
    }

}
