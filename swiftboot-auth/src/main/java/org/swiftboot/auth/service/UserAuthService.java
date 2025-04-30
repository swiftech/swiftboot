package org.swiftboot.auth.service;

import org.swiftboot.common.auth.response.LogoutResponse;
import org.swiftboot.common.auth.token.Authenticated;

/**
 * The authentication service for user login with user login id and password.
 * This interface must be implemented and be invoked by you.
 *
 * @param <T>
 * @author swiftech
 * @see Authenticated
 * @since 2.2
 */
public interface UserAuthService<T extends Authenticated> {

    /**
     * Authenticate user and return a valid {@link Authenticated} object if success.
     *
     * @param loginId
     * @param loginPwd
     * @return
     */
    T userSignIn(String loginId, String loginPwd);

    /**
     * Refresh the user's Access Token, Used only for JWT mode.
     *
     * @param refreshToken
     */
    T refreshAccessToken(String refreshToken);

    /**
     * User logout
     * TODO
     * @param accessToken
     */
    <R> LogoutResponse<R> userLogout(String accessToken);

}
