package org.swiftboot.auth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.swiftboot.auth.model.UserPersistable;

import java.util.Optional;

/**
 * User authentication repository that needs to be extended when using default implementation of {@link org.swiftboot.auth.service.UserAuthService}
 *
 * @see org.swiftboot.auth.service.UserAuthService
 * @see org.swiftboot.auth.service.impl.DefaultUserJwtAuthService
 * @see org.swiftboot.auth.service.impl.DefaultUserSessionAuthService
 * @since 3.0
 */
@NoRepositoryBean
public interface UserAuthRepository<T extends UserPersistable> extends CrudRepository<T, String> {

    /**
     * Query user by Login name and password.
     *
     * @param loginName Login name
     * @param loginPwd  Login pwd
     * @return
     */
    Optional<T> findByLoginNameAndLoginPwd(String loginName, String loginPwd);

    /**
     * Query user by login name.
     *
     * @param loginName
     * @return
     */
    Optional<T> findByLoginName(String loginName);
}
