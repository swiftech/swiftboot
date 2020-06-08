package org.swiftboot.shiro.service;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * 密码管理接口
 *
 * @author swiftech
 * @since 1.2
 */
public interface PasswordManager {

    /**
     * 生成密码
     *
     * @param originalPwd
     * @param source      生成密码的请求来源
     * @return
     */
    String encryptPassword(String originalPwd, String source);

    /**
     * 通过 Shiro 的 UsernamePasswordToken 生成密码
     *
     * @param usernamePasswordToken
     * @return
     */
    String encryptPassword(UsernamePasswordToken usernamePasswordToken);
}
