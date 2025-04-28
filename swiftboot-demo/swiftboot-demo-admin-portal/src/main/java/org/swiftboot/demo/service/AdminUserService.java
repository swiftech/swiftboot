package org.swiftboot.demo.service;

import org.swiftboot.demo.request.AdminUserCreateCommand;
import org.swiftboot.demo.request.AdminUserSaveCommand;
import org.swiftboot.demo.request.AdminUserSigninCommand;
import org.swiftboot.demo.request.AdminUserSignoutCommand;
import org.swiftboot.demo.result.*;
import org.swiftboot.web.request.IdListCommand;
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
    AdminUserSigninResult adminUserSignin(AdminUserSigninCommand command);

    /**
     * Admin user signin
     *
     * @param command
     * @return
     */
    AdminUserSignoutResult adminUserSignout(AdminUserSignoutCommand command);

    /**
     * 创建管理员
     *
     * @param cmd
     * @return
     */
    AdminUserCreateResult createAdminUser(AdminUserCreateCommand cmd);

    /**
     * 保存对管理员的修改
     *
     * @param cmd
     * @return
     */
    AdminUserSaveResult saveAdminUser(AdminUserSaveCommand cmd);

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
    void deleteAdminUserList(IdListCommand cmd);


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
    void purgeAdminUserList(IdListCommand cmd);


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