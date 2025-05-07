package org.swiftboot.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.swiftboot.demo.model.entity.AdminUserEntity;

import java.util.List;
import java.util.Optional;

/**
 * 管理员数据访问接口
 *
 * @author swiftech 2020-01-06
 **/
public interface AdminUserDao extends PagingAndSortingRepository<AdminUserEntity, String>, CrudRepository<AdminUserEntity, String>,
        AdminUserCustomizeDao {

    /**
     * 按照登录名,登录密码查询管理员
     *
     * @param loginName 登录名
     * @param loginPwd  登录密码
     * @return
     */
    Optional<AdminUserEntity> findByLoginNameAndLoginPwd(String loginName, String loginPwd);

    /**
     * 按照登录名查询管理员
     *
     * @param loginName 登录名
     * @return
     */
    Optional<AdminUserEntity> findByLoginName(String loginName);

    /**
     * 按照登录名查询未逻辑删除的管理员
     *
     * @param loginName 登录名
     * @return
     */
    Optional<AdminUserEntity> findByIsDeleteFalseAndLoginName(String loginName);

    /**
     * 批量按照ID查询管理员
     *
     * @param ids ID列表
     * @return
     */
    List<AdminUserEntity> findAllByIdIn(List<String> ids);

    /**
     * 查询所有非逻辑删除的管理员
     *
     * @return
     */
    List<AdminUserEntity> findAllByIsDeleteFalse();


    /**
     * 统计非逻辑删除的管理员总数
     *
     * @return
     */
    long countByIsDeleteFalse();

}
