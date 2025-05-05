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

import java.util.Optional;

/**
 * @author swiftech
 */
public class DefaultUserSessionAuthService<T extends UserPersistable> implements UserAuthService<Session> {

    private static final Logger log = LoggerFactory.getLogger(DefaultUserSessionAuthService.class);

    @Resource
    private UserAuthRepository<T> userAuthRepository;

    @Resource
    private AuthConfigBean authConfig;

    @Override
    public Session userSignIn(String loginId, String loginPwd) {
        String encryptedPwd = PasswordUtils.createPassword(loginPwd, authConfig.getPasswordSalt());
        Optional<T> optUser = userAuthRepository.findByLoginNameAndLoginPwd(loginId, encryptedPwd);
        if (optUser.isPresent()) {
            T appUserEntity = optUser.get();
            log.debug(appUserEntity.getId());
            // session
            String token = IdUtils.makeID("usrtoken");
            Session session = new SessionBuilder()
                    .userToken(token)
                    .userId(appUserEntity.getId())
                    .userName(appUserEntity.getLoginName())
                    .addition("login_time", DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss"))
                    .createSession();
            return session;
        }
        else {
            log.debug("Sign in failed for user: " + loginId);
            throw new ErrMessageException(ResponseCode.CODE_SIGNIN_FAIL);
        }
    }

    @Override
    public Session refreshAccessToken(String refreshToken) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public LogoutResponse<String> userLogout(String accessToken) {
        LogoutResponse<String> logoutResponse = new LogoutResponse<>(accessToken);
        return logoutResponse;
    }
}
