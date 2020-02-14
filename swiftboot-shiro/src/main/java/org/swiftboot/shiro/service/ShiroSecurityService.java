package org.swiftboot.shiro.service;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.swiftboot.web.exception.ErrMessageException;

/**
 * 用户认证授权实现接口，为不同的帐号体系提供统一的认证实现。
 * 具体的用户 Dao 实现 {@link org.swiftboot.shiro.model.dao.UserAuthDaoStub} 接口
 *
 * @author swiftech
 * @see org.swiftboot.shiro.model.dao.UserAuthDaoStub
 * @since 1.2
 **/
public interface ShiroSecurityService {

    /**
     * 验证系统用户身份
     *
     * @param usernamePasswordToken
     * @return 用户ID
     *
     * @throws ErrMessageException
     */
    String verifyUser(UsernamePasswordToken usernamePasswordToken) throws ErrMessageException;

    /**
     * 获取用户的授权信息（从视图获取，无需注解式事务）
     *
     * @param principalCollection
     * @return
     */
    SimpleAuthorizationInfo makeAuthInfoFromView(PrincipalCollection principalCollection);
}
