package org.swiftboot.demo.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.auth.controller.AuthenticatedResponse;
import org.swiftboot.common.auth.JwtTokenProvider;
import org.swiftboot.auth.service.Session;
import org.swiftboot.auth.service.SessionBuilder;
import org.swiftboot.auth.service.UserAuthService;
import org.swiftboot.demo.model.dao.AppUserDao;
import org.swiftboot.demo.model.entity.AppUserEntity;
import org.swiftboot.util.CryptoUtils;
import org.swiftboot.util.PasswordUtils;
import org.swiftboot.web.exception.ErrMessageException;
import org.swiftboot.web.exception.ErrorCodeSupport;

import java.util.Optional;

/**
 * since 3.0.0
 */
public class AppUserJwtAuthServiceImpl implements UserAuthService {

    private static final Logger log = LoggerFactory.getLogger(AppUserJwtAuthServiceImpl.class);

    @Resource
    private AppUserDao appUserDao;

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    @PostConstruct
    public void initData() {
        // create a new user for testing
        Optional<AppUserEntity> optUser = appUserDao.findByLoginName("13866669999");
        if (optUser.isEmpty()){
            AppUserEntity newEntity = new AppUserEntity();
            newEntity.setLoginName("13866669999");
            newEntity.setLoginPwd(PasswordUtils.createPassword("12345678"));
            appUserDao.save(newEntity);
        }
    }

    @Override
    public <T> AuthenticatedResponse<T> userSignIn(String loginId, String loginPwd) {
        String encryptedPwd = CryptoUtils.md5(loginPwd);
        Optional<AppUserEntity> optUser = appUserDao.findByLoginNameAndLoginPwd(loginId, encryptedPwd);
        if (optUser.isPresent()) {
            log.debug(optUser.get().getId());
            String accessToken = jwtTokenProvider.generateAccessToken(loginId);
            Session session = new SessionBuilder()
                    .userToken(accessToken)
                    .userId(optUser.get().getId())
                    .userName(optUser.get().getLoginName())
                    .addition("some_addition", "some addition")
                    .createSession();
            return new AuthenticatedResponse<>(session);
        }
        else {
            log.debug("Sign in failed for user: " + loginId);
            throw new ErrMessageException(ErrorCodeSupport.CODE_SIGNIN_FAIL);
        }
    }
}
