package org.swiftboot.auth.model;

/**
 * To authenticate user by default {@link org.swiftboot.auth.service.UserAuthService} implementation from SwiftBoot-Auth,
 * your user entity must implement this interface.
 *
 * @see org.swiftboot.auth.service.UserAuthService
 * @see org.swiftboot.auth.repository.UserAuthRepository
 * @since 3.0
 */
public interface UserPersistable {

    /**
     * User ID
     * @return
     */
    String getId();

    /**
     * User login name that identify this user.
     *
     * @return
     */
    String getLoginName();

    /**
     * User password
     * @return
     */
    String getLoginPwd();

}
