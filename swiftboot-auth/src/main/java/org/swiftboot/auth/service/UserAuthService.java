package org.swiftboot.auth.service;

/**
 * The authentication service for user login with user id and password.
 * This interface must be implemented and be invoked by you.
 *
 * @author swiftech
 * @see Session
 * @since 2.2
 */
public interface UserAuthService {

    /**
     * Authenticate user and return a valid {@link Session} object if success.
     *
     * @param loginId
     * @param loginPwd
     * @return
     */
    <T> AuthenticatedResponse<T> userSignIn(String loginId, String loginPwd);
}
