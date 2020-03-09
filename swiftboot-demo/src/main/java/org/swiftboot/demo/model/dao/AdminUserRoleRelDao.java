package org.swiftboot.demo.model.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.swiftboot.demo.model.entity.AdminRoleEntity;
import org.swiftboot.demo.model.entity.AdminUserEntity;
import org.swiftboot.demo.model.entity.AdminUserRoleRelEntity;

import java.util.List;
import java.util.Optional;

/**
 * 用户角色关联数据访问接口
 *
 * @author swiftech
 **/
public interface AdminUserRoleRelDao extends PagingAndSortingRepository<AdminUserRoleRelEntity, String> {


    Optional<AdminUserRoleRelEntity> findByAdminRoleAndAdminUser(AdminRoleEntity adminRole, AdminUserEntity adminUser);

//    /**
//     * 按照用户唯一标识查询用户角色关联
//     *
//     * @param userId 用户唯一标识
//     * @return
//     */
//    List<AdminUserRoleRelEntity> findByUserId(String userId);
//
//    /**
//     * 按照用户唯一标识查询未逻辑删除的用户角色关联
//     *
//     * @param userId 用户唯一标识
//     * @return
//     */
//    List<AdminUserRoleRelEntity> findByIsDeleteFalseAndUserId(String userId);

//    /**
//     * 按照角色唯一标识查询用户角色关联
//     *
//     * @param roleId 角色唯一标识
//     * @return
//     */
//    List<AdminUserRoleRelEntity> findByRoleId(String roleId);
//
//    /**
//     * 按照角色唯一标识查询未逻辑删除的用户角色关联
//     *
//     * @param roleId 角色唯一标识
//     * @return
//     */
//    List<AdminUserRoleRelEntity> findByIsDeleteFalseAndRoleId(String roleId);

    /**
     * 批量按照ID查询用户角色关联
     *
     * @param ids ID列表
     * @return
     */
    List<AdminUserRoleRelEntity> findAllByIdIn(List<String> ids);

    /**
     * 查询所有非逻辑删除的用户角色关联
     *
     * @return
     */
    List<AdminUserRoleRelEntity> findAllByIsDeleteFalse();

    /**
     * 删除两个关联表字段存在的关系
     *
     * @param adminUserId 用户ID
     * @param adminRoleId 角色ID
     */
    void deleteByAdminUserIdAndAdminRoleId(String adminUserId, String adminRoleId);

    /**
     * 统计非逻辑删除的用户角色关联总数
     *
     * @return
     */
    long countByIsDeleteFalse();

}
