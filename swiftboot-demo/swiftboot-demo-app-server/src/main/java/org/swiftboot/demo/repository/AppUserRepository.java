package org.swiftboot.demo.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.swiftboot.auth.repository.UserAuthRepository;
import org.swiftboot.demo.model.AppUserEntity;

import java.util.Optional;

/**
 * App用户数据访问接口
 *
 * @author swiftech 2020-01-06
 **/
public interface AppUserRepository extends PagingAndSortingRepository<AppUserEntity, String>
        , UserAuthRepository<AppUserEntity> {

    /**
     * 按照Login name of app user查询App用户
     *
     * @param loginName Login name of app user
     * @return
     */
    Optional<AppUserEntity> findByLoginName(String loginName);
}
