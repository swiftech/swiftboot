package org.swiftboot.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author swiftech 2019-05-20
 * @see SessionConfigBean
 **/
@Configuration
@ConfigurationProperties("swiftboot.auth")
public class AuthConfigBean {
    /**
     * 认证模式，'jwt' 或 'session'， 默认为 `jwt`
     */
    private String authType = "jwt";

    /**
     * 会话令牌的名称，作为客户端保存和传递时的 key
     */
    private String tokenKey = "access_token";

    /**
     * 密码加盐配置，不设置的话则不加盐
     */
    private String passwordSalt;

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getTokenKey() {
        return tokenKey;
    }

    public void setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

}
