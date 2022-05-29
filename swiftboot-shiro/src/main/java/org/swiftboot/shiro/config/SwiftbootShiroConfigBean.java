package org.swiftboot.shiro.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author swiftech
 * @since 1.2
 **/
@Configuration
@ConfigurationProperties("swiftboot.shiro")
public class SwiftbootShiroConfigBean {

    @NestedConfigurationProperty
    private CookieConfigBean cookie = new CookieConfigBean();

    @NestedConfigurationProperty
    private ShiroSessionConfigBean session = new ShiroSessionConfigBean();

    /**
     * 开启用户认证和鉴权功能（基于Shiro）
     */
    private boolean enabled = false;

    /**
     * 登录 URL，在认证失败的时候会跳转
     */
    private String loginUrl = "/login";

    /**
     * 登录认证成功之后跳转的 URL
     */
    private String successUrl = "";

    public CookieConfigBean getCookie() {
        return cookie;
    }

    public void setCookie(CookieConfigBean cookie) {
        this.cookie = cookie;
    }

    public ShiroSessionConfigBean getSession() {
        return session;
    }

    public void setSession(ShiroSessionConfigBean session) {
        this.session = session;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getSuccessUrl() {
        return successUrl;
    }

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

}
