package org.swiftboot.shiro.realm;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.swiftboot.shiro.SwiftbootShiroConfigBean;
import org.swiftboot.shiro.service.ShiroSecurityService;
import org.swiftboot.web.exception.ErrMessageException;

import javax.annotation.Resource;

import static org.swiftboot.shiro.constant.ShiroSessionConstants.SESSION_KEY_LOGIN_NAME;
import static org.swiftboot.shiro.constant.ShiroSessionConstants.SESSION_KEY_USER_ID;

/**
 * 用户认证安全域，处理系统用户认证和授权
 * <p>
 *
 * @author swiftech
 * @since 1.2
 */
@Transactional
public class UserAuthorizingRealm extends AuthorizingRealm {

    private Logger log = LoggerFactory.getLogger(UserAuthorizingRealm.class);

    @Resource
    private ShiroSecurityService shiroSecurityService;

    @Resource
    private SwiftbootShiroConfigBean shiroConfigBean;

    /**
     * Shiro 获取授权信息（无论何种登录方式）
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
            throws AuthenticationException {

        UsernamePasswordToken userPasswordToken = (UsernamePasswordToken) authenticationToken;
        log.info("Verify user: " + userPasswordToken.getUsername());

        // Verify user's password
        String userId;
        try {
            userId = shiroSecurityService.verifyUser(userPasswordToken);
        } catch (ErrMessageException e) {
            log.error(e.getMessage(), e);
            throw new AuthenticationException(e.getMessage());
        }

        log.info(String.format("User %s verified", userPasswordToken.getUsername()));

        SimpleAuthenticationInfo simpleAuthenticationInfo =
                new SimpleAuthenticationInfo(
                        userPasswordToken.getUsername(), // 用户名
                        userPasswordToken.getPassword(), // 密码
                        getName() //
                );

        try {
            // 根据用户 Cookie 中的 token 获取会话，不存在就创建一个新的
            Session session = SecurityUtils.getSubject().getSession();
            if (session == null) {
                session = SecurityUtils.getSubject().getSession(true);
                session.setTimeout(shiroConfigBean.getSession().getTimeout() * 1000);
                log.info(String.format("Session not exist, create a new one: %s", session.getId()));
            }
            // 保存操作人的 ID 等信息
            session.setAttribute(SESSION_KEY_USER_ID, userId);
            session.setAttribute(SESSION_KEY_LOGIN_NAME, userPasswordToken.getUsername());
//            log.info("Save user info to Shiro session: ：" + session.getId());
        } catch (Exception e) {
//            log.warn("Failed to save user info to Shiro session");
            log.error(e.getMessage(), e);
        }
        return simpleAuthenticationInfo;
    }

    /**
     * Shiro 获取授权信息（仅在常规登录的情况下有用，单点登录不从此获取授权信息，而是通过另外的 Realm）
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        log.debug("Fetch all permissions for current user");
        try {
            AuthorizationInfo simpleAuthorizationInfo = shiroSecurityService.makeAuthInfoFromView(principalCollection);
            if (simpleAuthorizationInfo == null
                    || simpleAuthorizationInfo.getStringPermissions() == null
                    || simpleAuthorizationInfo.getStringPermissions().isEmpty()) {
                log.warn("User haven't been granted any permissions");
                return null;
            }
            return simpleAuthorizationInfo;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
