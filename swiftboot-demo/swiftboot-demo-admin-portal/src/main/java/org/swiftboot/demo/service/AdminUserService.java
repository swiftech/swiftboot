package org.swiftboot.demo.service;

import org.springframework.transaction.annotation.Transactional;
import org.swiftboot.demo.dto.*;
import org.swiftboot.demo.request.AdminUserRequest;
import org.swiftboot.demo.request.AdminUserSigninRequest;
import org.swiftboot.demo.request.AdminUserSignoutRequest;
import org.swiftboot.web.request.IdListRequest;

/**
 * 管理员服务接口
 *
 * @author swiftech 2020-01-06
 **/
@Transactional
public interface AdminUserService {

    /**
     * Admin user signin
     *
     * @param command
     * @return
     */
    AdminUserSigninResult adminUserSignin(AdminUserSigninRequest command);

    /**
     * Admin user signin
     *
     * @param command
     * @return
     */
    AdminUserSignoutResult adminUserSignout(AdminUserSignoutRequest command);

    /**
     * 创建管理员
     *
     * @param request
     * @return
     */
    AdminUserCreateResult createAdminUser(AdminUserRequest request);

    /**
     * 保存对管理员的修改
     *
     * @param request
     * @return
     */
    AdminUserSaveResult saveAdminUser(String userId, AdminUserRequest request);

    /**
     * 逻辑删除管理员
     *
     * @param adminUserId
     */
    void deleteAdminUser(String adminUserId);

    /**
     * 批量逻辑删除管理员
     *
     * @param request
     */
    void deleteAdminUserList(IdListRequest request);


    /**
     * 永久删除管理员
     *
     * @param adminUserId
     */
    void purgeAdminUser(String adminUserId);

    /**
     * 批量永久删除管理员
     *
     * @param request
     */
    void purgeAdminUserList(IdListRequest request);


    /**
     * 查询管理员
     *
     * @param adminUserId
     * @return
     */
    AdminUserResult queryAdminUser(String adminUserId);

    /**
     * 查询所有管理员
     *
     * @return
     */
    AdminUserListResult queryAdminUserList();

    /**
     * 分页查询管理员
     *
     * @param page 页数，从0开始
     * @param pageSize 页大小
     * @return
     */
    AdminUserListResult queryAdminUserList(int page, int pageSize);


}