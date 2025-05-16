package org.swiftboot.auth.service;

import org.swiftboot.common.auth.response.LogoutResponse;
import org.swiftboot.common.auth.token.Authenticated;

import java.util.Map;

/**
 * The authentication service for user login with user login id and password.
 * SwiftBoot provides 2 default implementation {@link org.swiftboot.auth.service.impl.DefaultUserJwtAuthService}
 * and {@link org.swiftboot.auth.service.impl.DefaultUserSessionAuthService} to do simple authentication.
 * You can implement your own authentication service if you want to.
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

    T userSignIn(String loginId, String loginPwd, Map<String, Object> additions);

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
