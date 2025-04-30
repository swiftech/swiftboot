package org.swiftboot.auth.service.impl;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.auth.model.Session;
import org.swiftboot.auth.model.SessionBuilder;
import org.swiftboot.auth.model.UserPersistable;
import org.swiftboot.auth.repository.UserAuthRepository;
import org.swiftboot.auth.service.UserAuthService;
import org.swiftboot.common.auth.response.LogoutResponse;
import org.swiftboot.common.auth.token.Authenticated;
import org.swiftboot.util.CryptoUtils;
import org.swiftboot.util.IdUtils;
import org.swiftboot.web.exception.ErrMessageException;
import org.swiftboot.web.response.ResponseCode;

import java.util.Optional;

/**
 * @author swiftech
 */
public class DefaultUserSessionAuthService<T extends UserPersistable> implements UserAuthService {

    private static final Logger log = LoggerFactory.getLogger(DefaultUserSessionAuthService.class);

    @Resource
    private UserAuthRepository<T> appUserRepository;

    @Override
    public Authenticated userSignIn(String loginId, String loginPwd) {
        String encryptedPwd = CryptoUtils.md5(loginPwd);
        Optional<T> optUser = appUserRepository.findByLoginNameAndLoginPwd(loginId, encryptedPwd);
        if (optUser.isPresent()) {
            T appUserEntity = optUser.get();
            log.debug(optUser.get().getId());
            // session
            String token = IdUtils.makeID("usrtoken");
            Session session = new SessionBuilder()
                    .userToken(token)
                    .userId(optUser.get().getId())
                    .userName(optUser.get().getLoginName())
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
    public Authenticated refreshAccessToken(String refreshToken) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public LogoutResponse<String> userLogout(String accessToken) {
        LogoutResponse<String> logoutResponse = new LogoutResponse<>(accessToken);
        return logoutResponse;
    }
}
