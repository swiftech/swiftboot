package org.swiftboot.demo.shiro;

import org.swiftboot.demo.constant.AuthConstants;
import org.swiftboot.demo.repository.AdminUserDao;
import org.swiftboot.demo.model.entity.AdminUserEntity;
import org.springframework.stereotype.Component;
import org.swiftboot.shiro.model.dao.UserAuthDaoStub;

import jakarta.annotation.Resource;
import java.util.Optional;

/**
 *
 * @author swiftech
 * @deprecated
 */
@Component(AuthConstants.MY_AUTH_SERVICE_NAME)
public class DemoUserAuthDao implements UserAuthDaoStub<AdminUserEntity> {

    @Resource
    private AdminUserDao adminUserDao;

    @Override
    public AdminUserEntity findByLoginName(String loginName) {
        Optional<AdminUserEntity> optAdminUser = adminUserDao.findByLoginName(loginName);
        return optAdminUser.orElse(null);
    }
}
