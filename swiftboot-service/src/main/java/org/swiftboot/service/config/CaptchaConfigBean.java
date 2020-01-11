package org.swiftboot.service.config;

/**
 * swiftboot.service.captcha
 *
 * @author swiftech
 */
public class CaptchaConfigBean {

    /**
     * Captcha 超时时间，单位是秒，默认为 5 分钟
     */
    int expiresIn = 5 * 60;

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}
