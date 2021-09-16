package org.swiftboot.demo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.swiftboot.auth.SwiftbootAuthConfigBean;
import org.swiftboot.auth.service.Session;
import org.swiftboot.auth.service.SessionBuilder;
import org.swiftboot.auth.service.SessionService;
import org.swiftboot.demo.command.AppUserSigninCommand;
import org.swiftboot.demo.model.dao.AppUserDao;
import org.swiftboot.demo.model.entity.AppUserEntity;
import org.swiftboot.demo.result.AppUserSigninResult;
import org.swiftboot.demo.service.AppUserService;
import org.swiftboot.util.CryptoUtils;
import org.swiftboot.util.IdUtils;
import org.swiftboot.util.PasswordUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author swiftech 2020-02-05
 */
@Service
public class AppUserServiceImpl implements AppUserService {

    private final Logger log = LoggerFactory.getLogger(AppUserServiceImpl.class);

    @Resource
    private AppUserDao appUserDao;

    @Resource
    private SessionService sessionService;

    @Resource
    private SwiftbootAuthConfigBean authConfigBean;

    @PostConstruct
    public void initData() {
        Optional<AppUserEntity> optUser = appUserDao.findByLoginName("13866669999");
        if (!optUser.isPresent()){
            AppUserEntity newEntity = new AppUserEntity();
            newEntity.setLoginName("13866669999");
            newEntity.setLoginPwd(PasswordUtils.createPassword("12345678"));
            appUserDao.save(newEntity);
        }
    }

    @Override
    public AppUserSigninResult appUserSignin(AppUserSigninCommand command) {
        AppUserSigninResult ret = new AppUserSigninResult();
        String encryptedPwd = CryptoUtils.md5(command.getLoginPwd());
        Optional<AppUserEntity> optUser = appUserDao.findByLoginNameAndLoginPwd(command.getLoginName(), encryptedPwd);
        if (optUser.isPresent()) {
            log.debug(optUser.get().getId());
            ret.setLoginName(optUser.get().getLoginName());
            ret.setId(optUser.get().getId());
            ret.setUpdateTime(optUser.get().getUpdateTime());
            ret.setSuccess(true);

            // session
            Session session = new SessionBuilder()
                    .userName(ret.getLoginName())
                    .userId(ret.getId())
                    .group(authConfigBean.getSession().getGroup())
                    .addition("some_addition", "some addition in session")
                    .createSession();
            String token = IdUtils.makeUUID();
            sessionService.addSession(token, session);
            ret.setToken(token);
        }
        else {
            log.debug("Signin failed: " + command.getLoginName());
        }
        return ret;
    }
}
