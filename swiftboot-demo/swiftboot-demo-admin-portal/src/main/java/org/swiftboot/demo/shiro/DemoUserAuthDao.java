package org.swiftboot.demo.shiro;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.swiftboot.demo.constant.AuthConstants;
import org.swiftboot.demo.model.AdminUserEntity;
import org.swiftboot.demo.repository.AdminUserRepository;

import java.util.Optional;

/**
 *
 * @author swiftech
 * @deprecated
 */
@Component(AuthConstants.MY_AUTH_SERVICE_NAME)
public class DemoUserAuthDao {//implements UserAuthDaoStub<AdminUserEntity> {

    @Resource
    private AdminUserRepository adminUserRepository;

//    @Override
    public AdminUserEntity findByLoginName(String loginName) {
        Optional<AdminUserEntity> optAdminUser = adminUserRepository.findByLoginName(loginName);
        return optAdminUser.orElse(null);
    }
}
