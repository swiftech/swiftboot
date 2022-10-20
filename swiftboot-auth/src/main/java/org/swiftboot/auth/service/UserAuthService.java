package org.swiftboot.auth.service;

import org.swiftboot.auth.controller.AuthenticatedResponse;

/**
 * The authentication service for user weill implemented and invoked by you.
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
