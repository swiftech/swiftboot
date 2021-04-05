package org.swiftboot.shiro;

import org.springframework.stereotype.Component;
import org.swiftboot.shiro.model.dao.UserAuthDaoStub;
import org.swiftboot.shiro.service.PasswordManager;

import javax.annotation.Resource;

/**
 * @author swiftech
 */
@Component("test-auth")
public class UserAuthDao implements UserAuthDaoStub<UserEntity> {

    @Resource
    PasswordManager passwordManager;

    @Override
    public UserEntity findByLoginName(String loginName) {
        UserEntity userEntity = new UserEntity("10001", loginName);
        String pwd;
        if (TestConstants.adminUser.equals(loginName)){
            pwd = TestConstants.adminPassword;
        }
        else {
            pwd = TestConstants.staffPassword;
        }
        String encryptPassword = passwordManager.encryptPassword(pwd, "test-auth");
        userEntity.setLoginPwd(encryptPassword);
        return userEntity;
    }
}
