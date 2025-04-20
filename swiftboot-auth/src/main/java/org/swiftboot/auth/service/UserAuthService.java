package org.swiftboot.auth.service;

import org.swiftboot.auth.model.Authenticated;

/**
 * The authentication service for user login with user id and password.
 * This interface must be implemented and be invoked by you.
 *
 * @author swiftech
 * @see Authenticated
 * @since 2.2
 */
public interface UserAuthService {

    /**
     * Authenticate user and return a valid {@link Authenticated} object if success.
     *
     * @param loginId
     * @param loginPwd
     * @return
     */
    <R> AuthenticatedResponse<R, Authenticated> userSignIn(String loginId, String loginPwd);

    /**
     * Refresh the user's Access Token, Used only for JWT mode.
     *
     * @param refreshToken
     */
    <R> AuthenticatedResponse<R, Authenticated> refreshAccessToken(String refreshToken);
}
