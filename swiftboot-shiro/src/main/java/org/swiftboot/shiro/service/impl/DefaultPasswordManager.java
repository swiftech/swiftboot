package org.swiftboot.shiro.service.impl;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.shiro.service.PasswordManager;
import org.swiftboot.util.CryptoUtils;

import javax.annotation.PostConstruct;

/**
 * 默认的密码管理器，用 md5 来生成加密密码，不同的应用需实现自己的密码管理器，一旦实现并加载为 bean ，此默认管理器失效。
 *
 * @since 1.2
 */
public class DefaultPasswordManager implements PasswordManager {

    private Logger log = LoggerFactory.getLogger(DefaultPasswordManager.class);

    @PostConstruct
    public void init() {
        // 启用默认的密码管理器，如果需要更加安全，定义自己的密码管理并且实现更复杂的密码机制
        log.warn("");
    }

    /**
     * 默认使用 source 作为加盐
     *
     * @param originalPwd
     * @param source      生成密码的请求来源
     * @return
     */
    @Override
    public String encryptPassword(String originalPwd, String source) {
        return CryptoUtils.md5salt(originalPwd, source);
    }

    /**
     * 默认使用 UsernamePasswordToken 的 host 值作为加盐
     *
     * @param usernamePasswordToken
     * @return
     */
    @Override
    public String encryptPassword(UsernamePasswordToken usernamePasswordToken) {
        return CryptoUtils.md5salt(String.valueOf(usernamePasswordToken.getPassword()), usernamePasswordToken.getHost());
    }
}
