package org.swiftboot.auth.service.impl;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.auth.config.AuthConfigBean;
import org.swiftboot.auth.model.Session;
import org.swiftboot.auth.model.SessionBuilder;
import org.swiftboot.auth.model.UserPersistable;
import org.swiftboot.auth.repository.UserAuthRepository;
import org.swiftboot.auth.service.UserAuthService;
import org.swiftboot.common.auth.response.LogoutResponse;
import org.swiftboot.util.IdUtils;
import org.swiftboot.util.PasswordUtils;
import org.swiftboot.web.exception.ErrMessageException;
import org.swiftboot.web.response.ResponseCode;

import java.util.Map;
import java.util.Optional;

/**
 *
 * @param <E> Type of user entity inherited from {@link UserPersistable}.
 * @author swiftech
 */
public class DefaultUserSessionAuthService<E extends UserPersistable> implements UserAuthService<Session> {

    private static final Logger log = LoggerFactory.getLogger(DefaultUserSessionAuthService.class);

    @Resource
    protected UserAuthRepository<E> userAuthRepository;

    @Resource
    protected AuthConfigBean authConfig;

    @Override
    public Session userSignIn(String loginId, String loginPwd) {
        return this.userSignIn(loginId, loginPwd, null);
    }

    @Override
    public Session userSignIn(String loginId, String loginPwd, Map<String, Object> additions) {
        String encryptedPwd = PasswordUtils.createPassword(loginPwd, authConfig.getPasswordSalt());
        Optional<E> optUser = userAuthRepository.findByLoginNameAndLoginPwd(loginId, encryptedPwd);
        if (optUser.isPresent()) {
            E userEntity = optUser.get();
            log.debug(userEntity.getId());
            // session
            String token = IdUtils.makeID("usrtoken");
            SessionBuilder sessionBuilder = new SessionBuilder()
                    .userToken(token)
                    .userId(userEntity.getId())
                    .userName(userEntity.getLoginName())
                    .addition("login_time", DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"));
            if (additions != null && !additions.isEmpty()) {
                additions.forEach(sessionBuilder::addition);
            }
            return sessionBuilder.createSession();
        }
        else {
            log.debug("Sign in failed for user: %s".formatted(loginId));
            throw new ErrMessageException(ResponseCode.CODE_SIGNIN_FAIL);
        }
    }

    @Override
    public Session refreshAccessToken(String refreshToken) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public LogoutResponse<String> userLogout(String accessToken) {
        return new LogoutResponse<>(accessToken);
    }
}
