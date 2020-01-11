package org.swiftboot.service.model;

import java.io.Serializable;

/**
 * 防机器人验证码
 *
 * @author swiftech
 */
public class Captcha implements Serializable {

    private String captcha;

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

}
