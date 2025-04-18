package org.swiftboot.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @author swiftech 2019-05-20
 * @see SessionConfigBean
 **/
@Configuration
@ConfigurationProperties("swiftboot.auth")
public class SwiftbootAuthConfigBean {

    /**
     * 开启用户认证功能，默认为不开启
     */
    private boolean enabled = false;

    /**
     * type of authentication, use 'jwt' or 'session'
     */
    private String authType;

    /**
     * 会话令牌的名称，作为客户端保存和传递时的 key
     */
    private String tokenKey = "access_token";

    /**
     * 会话配置
     */
    @NestedConfigurationProperty
    private SessionConfigBean session = new SessionConfigBean();

//    @NestedConfigurationProperty
//    private JwtConfigBean jwt = new JwtConfigBean();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

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

    public SessionConfigBean getSession() {
        return session;
    }

    public void setSession(SessionConfigBean session) {
        this.session = session;
    }

//    public JwtConfigBean getJwt() {
//        return jwt;
//    }
//
//    public void setJwt(JwtConfigBean jwt) {
//        this.jwt = jwt;
//    }
}
