package org.swiftboot.demo.model.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.swiftboot.demo.model.entity.AdminPermissionEntity;
import org.swiftboot.demo.model.entity.AdminRoleEntity;
import org.swiftboot.demo.model.entity.AdminRolePermissionRelEntity;

import java.util.List;
import java.util.Optional;

/**
 * 角色权限关联数据访问接口
 *
 * @author swiftech
 **/
public interface AdminRolePermissionRelDao extends PagingAndSortingRepository<AdminRolePermissionRelEntity, String> {


    Optional<AdminRolePermissionRelEntity> findByAdminRoleAndAdminPermission(AdminRoleEntity adminRole, AdminPermissionEntity adminPermission);

    /**
     * 查询所有角色权限关联
     *
     * @return
     */
    List<AdminRolePermissionRelEntity> findAll();


    /**
     * 批量按照ID查询角色权限关联
     *
     * @param ids ID列表
     * @return
     */
    List<AdminRolePermissionRelEntity> findAllByIdIn(List<String> ids);

    /**
     * 查询所有非逻辑删除的角色权限关联
     *
     * @return
     */
    List<AdminRolePermissionRelEntity> findAllByIsDeleteFalse();


    /**
     * 统计非逻辑删除的角色权限关联总数
     *
     * @return
     */
    long countByIsDeleteFalse();

}
