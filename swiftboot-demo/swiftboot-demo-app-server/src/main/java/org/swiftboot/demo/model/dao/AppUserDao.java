package org.swiftboot.demo.model.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.swiftboot.demo.model.entity.AppUserEntity;

import java.util.Optional;

/**
 * App用户数据访问接口
 *
 * @author swiftech 2020-01-06
 **/
public interface AppUserDao extends PagingAndSortingRepository<AppUserEntity, String>, CrudRepository<AppUserEntity, String>,
        AppUserCustomizeDao {

    /**
     * Query app user by Login name and password of app user
     *
     * @param loginName Login name of administrator
     * @param loginPwd  Login pwd
     * @return
     */
    Optional<AppUserEntity> findByLoginNameAndLoginPwd(String loginName, String loginPwd);

    /**
     * 按照Login name of app user查询App用户
     *
     * @param loginName Login name of app user
     * @return
     */
    Optional<AppUserEntity> findByLoginName(String loginName);
}
