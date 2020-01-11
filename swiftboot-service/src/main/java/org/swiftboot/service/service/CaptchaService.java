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
     *
     * @param captchaId
     * @return
     */
    String getCaptchaText(String captchaId);

    /**
     * 校验 Captcha
     *
     * @param captchaId
     * @param captcha
     * @return
     */
    boolean verifyCaptcha(String captchaId, String captcha);

}
