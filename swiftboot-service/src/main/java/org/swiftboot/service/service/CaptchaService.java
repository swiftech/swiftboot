package org.swiftboot.service.service;

/**
 * @author swiftech
 */
public interface CaptchaService {

    /**
     * 创建并保存 Captcha
     *
     * @param group
     * @return captcha ID
     */
    String createCaptcha(String group);

    /**
     * 创建并保存有效时间的 Captcha
     *
     * @param group
     * @param expires 超时时间，单位秒
     * @return captcha ID
     */
    String createCaptcha(String group, int expires);

    /**
     * 获取 Captcha 文字内容
     * @param group
     * @param captchaId
     * @return
     */
    String getCaptchaText(String group, String captchaId);

    /**
     * 校验 Captcha，校验后失效
     * @param group
     * @param captchaId
     * @param captcha
     * @return
     */
    boolean verifyCaptcha(String group, String captchaId, String captcha);

}
