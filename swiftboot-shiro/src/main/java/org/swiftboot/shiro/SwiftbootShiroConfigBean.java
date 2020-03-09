package org.swiftboot.shiro;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import org.swiftboot.shiro.config.CookieConfigBean;
import org.swiftboot.shiro.config.ShiroSessionConfigBean;

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

    private String loginUrl;

    private String returnUrl;

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

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }
}
