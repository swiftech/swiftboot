package org.swiftboot.demo.service;

import org.swiftboot.demo.request.AdminUserCreateRequest;
import org.swiftboot.demo.request.AdminUserSaveRequest;
import org.swiftboot.demo.request.AdminUserSigninRequest;
import org.swiftboot.demo.request.AdminUserSignoutRequest;
import org.swiftboot.demo.dto.*;
import org.swiftboot.web.request.IdListRequest;
import org.springframework.transaction.annotation.Transactional;

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
     * @param cmd
     * @return
     */
    AdminUserCreateResult createAdminUser(AdminUserCreateRequest cmd);

    /**
     * 保存对管理员的修改
     *
     * @param cmd
     * @return
     */
    AdminUserSaveResult saveAdminUser(AdminUserSaveRequest cmd);

    /**
     * 逻辑删除管理员
     *
     * @param adminUserId
     */
    void deleteAdminUser(String adminUserId);

    /**
     * 批量逻辑删除管理员
     *
     * @param cmd
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
     * @param cmd
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