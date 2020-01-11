package org.swiftboot.service.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.swiftboot.service.SwiftbootServiceConfigBean;
import org.swiftboot.service.model.Captcha;
import org.swiftboot.service.service.CaptchaService;
import org.swiftboot.service.service.RedisService;
import org.swiftboot.service.util.CaptchaUtils;
import org.swiftboot.util.IdUtils;

import javax.annotation.Resource;

/**
 *
 * @author swiftech
 */
public class CaptchaServiceImpl implements CaptchaService {

    private Logger log = org.slf4j.LoggerFactory.getLogger(CaptchaServiceImpl.class);

    @Resource
    private RedisService redisService;

    @Resource
    private SwiftbootServiceConfigBean serviceConfigBean;

    @Override
    public String createCaptcha(String group) {
        return this.createCaptcha(group, serviceConfigBean.getCaptcha().getExpiresIn());
    }

    @Override
    public String createCaptcha(String group, int expires) {
        String captchaId = IdUtils.makeID(group);
        String captchaText = CaptchaUtils.makeCaptcha(4);
        Captcha captcha = new Captcha();
        captcha.setCaptcha(captchaText);
        redisService.setObject(captchaId, expires, captcha);
        return captchaId;
    }

    @Override
    public boolean verifyCaptcha(String captchaId, String captcha) {
        if (StringUtils.isBlank(captcha)) {
            return false;
        }
        Object object = redisService.getObject(captchaId);
        if (object instanceof Captcha) {
            return ((Captcha) object).getCaptcha().equals(captcha);
        }
        else {
            return false;
        }
    }

    @Override
    public String getCaptchaText(String captchaId) {
        Object object = redisService.getObject(captchaId);
        if (object instanceof Captcha) {
            return ((Captcha) object).getCaptcha();
        }
        else {
            return null;
        }
    }

}
