package org.swiftboot.service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @author swiftech 2019-05-20
 **/
@Configuration
@ConfigurationProperties("swiftboot.service")
public class SwiftbootServiceConfigBean {

//    @NestedConfigurationProperty
//    private RedisConfigBean redis = new RedisConfigBean();

    @NestedConfigurationProperty
    private CaptchaConfigBean captcha = new CaptchaConfigBean();

//    public RedisConfigBean getRedis() {
//        return redis;
//    }
//
//    public void setRedis(RedisConfigBean redis) {
//        this.redis = redis;
//    }

    public CaptchaConfigBean getCaptcha() {
        return captcha;
    }

    public void setCaptcha(CaptchaConfigBean captcha) {
        this.captcha = captcha;
    }

}
