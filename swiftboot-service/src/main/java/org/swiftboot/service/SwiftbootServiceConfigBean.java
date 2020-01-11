package org.swiftboot.service;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.swiftboot.service.config.CaptchaConfigBean;
import org.swiftboot.service.config.RedisConfigBean;

/**
 * @author swiftech 2019-05-20
 **/
@ConfigurationProperties("swiftboot.service")
public class SwiftbootServiceConfigBean {

    @NestedConfigurationProperty
    private RedisConfigBean redis = new RedisConfigBean();

    @NestedConfigurationProperty
    private CaptchaConfigBean captcha = new CaptchaConfigBean();

    public RedisConfigBean getRedis() {
        return redis;
    }

    public void setRedis(RedisConfigBean redis) {
        this.redis = redis;
    }

    public CaptchaConfigBean getCaptcha() {
        return captcha;
    }

    public void setCaptcha(CaptchaConfigBean captcha) {
        this.captcha = captcha;
    }

}
