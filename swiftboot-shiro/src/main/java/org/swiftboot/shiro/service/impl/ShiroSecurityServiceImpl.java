package org.swiftboot.shiro.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.dao.DataAccessException;
import org.swiftboot.shiro.model.dao.UserAuthDaoStub;
import org.swiftboot.shiro.model.dao.UserPermissionDaoStub;
import org.swiftboot.shiro.model.entity.PermissionEntityStub;
import org.swiftboot.shiro.model.entity.UserEntityStub;
import org.swiftboot.shiro.service.PasswordManager;
import org.swiftboot.shiro.service.ShiroSecurityService;
import org.swiftboot.web.exception.ErrMessageException;
import org.swiftboot.web.exception.ErrorCodeSupport;

import javax.annotation.Resource;
import java.util.List;

/**
 * 默认的系统用户认证实现
 *
 * @author swiftech
 * @since 1.2
 **/
public class ShiroSecurityServiceImpl implements ShiroSecurityService, ApplicationContextAware {
    private Logger log = LoggerFactory.getLogger(ShiroSecurityServiceImpl.class);

    private ApplicationContext applicationContext;

    @Resource
    private PasswordManager passwordManager;

    @Resource
    private UserPermissionDaoStub<? extends PermissionEntityStub> userPermissionDao;

    @Override
    public String verifyUser(UsernamePasswordToken usernamePasswordToken) throws ErrMessageException {
        UserEntityStub user = null;
        try {
            UserAuthDaoStub<?> dao = (UserAuthDaoStub<?>) applicationContext.getBean(usernamePasswordToken.getHost());
            log.info("The DAO implementation to do user authentication: " + dao.getClass().getName());
            String loginName = usernamePasswordToken.getUsername();
            String loginPwd = String.valueOf(usernamePasswordToken.getPassword());
            log.info(String.format("Authenticate user %s(%s)", usernamePasswordToken.getUsername(), usernamePasswordToken.getHost()));
            user = dao.findByLoginName(loginName);
            if (user == null) {
                throw new ErrMessageException(ErrorCodeSupport.CODE_NO_REG);
            }

            // 此处用 UsernamePasswordToken 可以支持不同 host 来源做不同的密码加密
            String toBeVerified = passwordManager.encryptPassword(usernamePasswordToken);
            if (!user.getLoginPwd().equals(toBeVerified)) {
                log.debug(String.format("Check password: %s[%s]", toBeVerified, loginPwd));
                log.debug(String.format("Encrypted pwd in DB: %s", user.getLoginPwd()));
                throw new ErrMessageException(ErrorCodeSupport.CODE_SIGNIN_FAIL);
            }
            return user.getId();
        } catch (DataAccessException e) {
            e.printStackTrace();
            throw new ErrMessageException(ErrorCodeSupport.CODE_SIGNIN_FAIL);
        }
    }

    @Override
    public SimpleAuthorizationInfo makeAuthInfoFromView(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        for (Object principal : principalCollection) {
            String loginName = (String) principal;
            log.info(String.format("  for user：%s", loginName));
            List<PermissionEntityStub> allPermissions = (List<PermissionEntityStub>) userPermissionDao.findPermissionsByLoginName(loginName);

            for (PermissionEntityStub perm : allPermissions) {
                simpleAuthorizationInfo.addStringPermission(perm.getPermissionCode());
            }
        }

        if (simpleAuthorizationInfo.getStringPermissions() != null) {
            log.info(String.format("    has %d permissions", simpleAuthorizationInfo.getStringPermissions().size()));
            log.debug(StringUtils.join(simpleAuthorizationInfo.getStringPermissions(), ','));
        }
        else {
            log.warn("    No permissions");
        }
        return simpleAuthorizationInfo;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
