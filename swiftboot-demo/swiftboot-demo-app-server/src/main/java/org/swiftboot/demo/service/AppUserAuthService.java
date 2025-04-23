package org.swiftboot.demo.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.common.auth.response.LogoutResponse;
import org.swiftboot.common.auth.token.Authenticated;
import org.swiftboot.common.auth.response.AuthenticatedResponse;
import org.swiftboot.auth.model.Session;
import org.swiftboot.auth.model.SessionBuilder;
import org.swiftboot.auth.service.UserAuthService;
import org.swiftboot.demo.dto.AppUserSignInDto;
import org.swiftboot.demo.repository.AppUserRepository;
import org.swiftboot.demo.model.AppUserEntity;
import org.swiftboot.util.CryptoUtils;
import org.swiftboot.util.IdUtils;
import org.swiftboot.util.PasswordUtils;
import org.swiftboot.web.exception.ErrMessageException;
import org.swiftboot.web.exception.ErrorCodeSupport;

import java.util.Optional;

/**
 * @author swiftech
 */
public class AppUserAuthService implements UserAuthService {

    private static final Logger log = LoggerFactory.getLogger(AppUserAuthService.class);

    @Resource
    private AppUserRepository appUserRepository;

    @PostConstruct
    public void initData() {
        // create a new user for testing
        Optional<AppUserEntity> optUser = appUserRepository.findByLoginName("13866669999");
        if (optUser.isEmpty()) {
            AppUserEntity newEntity = new AppUserEntity();
            newEntity.setLoginName("13866669999");
            newEntity.setLoginPwd(PasswordUtils.createPassword("12345678"));
            appUserRepository.save(newEntity);
        }
    }

    @Override
    public AuthenticatedResponse<AppUserSignInDto, Authenticated> userSignIn(String loginId, String loginPwd) {
        String encryptedPwd = CryptoUtils.md5(loginPwd);
        Optional<AppUserEntity> optUser = appUserRepository.findByLoginNameAndLoginPwd(loginId, encryptedPwd);
        if (optUser.isPresent()) {
            AppUserEntity appUserEntity = optUser.get();
            log.debug(optUser.get().getId());
            // session
            String token = IdUtils.makeID("usrtoken");
            Session session = new SessionBuilder()
                    .userToken(token)
                    .userId(optUser.get().getId())
                    .userName(optUser.get().getLoginName())
                    .addition("some_addition", "some addition in session")
                    .createSession();
            // DTO to client user
            AppUserSignInDto signInDto = new AppUserSignInDto();
            signInDto.setId(appUserEntity.getId());
            signInDto.setLoginName(appUserEntity.getLoginName());
            signInDto.setAccessToken(token);
            signInDto.setExpiresAt(session.getExpireTime());
            return new AuthenticatedResponse(signInDto, session);
        }
        else {
            log.debug("Sign in failed for user: " + loginId);
            throw new ErrMessageException(ErrorCodeSupport.CODE_SIGNIN_FAIL);
        }
    }

    @Override
    public AuthenticatedResponse<AppUserSignInDto, Authenticated> refreshAccessToken(String refreshToken) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public LogoutResponse<String> userLogout(String accessToken) {
        LogoutResponse<String> logoutResponse = new LogoutResponse<>(accessToken);
        return logoutResponse;
    }
}
