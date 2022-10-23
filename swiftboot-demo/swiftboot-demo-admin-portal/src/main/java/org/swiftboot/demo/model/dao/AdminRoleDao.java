package org.swiftboot.demo.model.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.swiftboot.demo.model.entity.AdminRoleEntity;

import java.util.List;
import java.util.Optional;

/**
 * 管理员用户角色数据访问接口
 *
 * @author swiftech
 **/
public interface AdminRoleDao extends PagingAndSortingRepository<AdminRoleEntity, String> {

    /**
     *
     */
    Optional<AdminRoleEntity> findByRoleName(String roleName);

    /**
     * 批量按照ID查询用户角色
     *
     * @param ids ID列表
     * @return
     */
    List<AdminRoleEntity> findAllByIdIn(List<String> ids);

    /**
     * 查询所有非逻辑删除的用户角色
     *
     * @return
     */
    List<AdminRoleEntity> findAllByIsDeleteFalse();


    /**
     * 统计非逻辑删除的用户角色总数
     *
     * @return
     */
    long countByIsDeleteFalse();

}
