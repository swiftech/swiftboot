package org.swiftboot.service.service.impl;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.swiftboot.service.config.SwiftbootServiceConfigBean;
import org.swiftboot.service.model.Captcha;
import org.swiftboot.service.service.CaptchaService;
import org.swiftboot.service.util.CaptchaUtils;
import org.swiftboot.util.IdUtils;

import java.time.Duration;

/**
 * @author swiftech
 */
public class CaptchaServiceImpl implements CaptchaService {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(CaptchaServiceImpl.class);

    @Resource
    private RedisTemplate<String, Captcha> redisTemplate;

    @Resource
    private SwiftbootServiceConfigBean serviceConfigBean;

    @PostConstruct
    public void init() {
        redisTemplate.setKeySerializer(StringRedisSerializer.UTF_8);
        redisTemplate.setValueSerializer(RedisSerializer.json());
    }

    @Override
    public String createCaptcha(String group) {
        return this.createCaptcha(group, serviceConfigBean.getCaptcha().getExpiresIn());
    }

    @Override
    public String createCaptcha(String group, int expires) {
        if (StringUtils.isBlank(group)) {
            throw new IllegalArgumentException("Captcha group is blank");
        }
        String captchaId = IdUtils.makeUUID();
        String captchaText = CaptchaUtils.makeCaptcha(4);
        Captcha captcha = new Captcha();
        captcha.setCaptcha(captchaText);
        redisTemplate.opsForValue().set("%s:%s".formatted(group, captchaId), captcha, Duration.ofSeconds(expires));
//        redisTemplate.opsForHash().expire(group, Duration.ofSeconds(expires), Collections.singleton(captchaId));
        return captchaId;
    }

    @Override
    public boolean verifyCaptcha(String group, String captchaId, String captcha) {
        if (StringUtils.isBlank(captcha)) {
            return false;
        }
        Object storedCaptcha = redisTemplate.opsForValue().get("%s:%s".formatted(group, captchaId));
        boolean success;
        if (storedCaptcha instanceof Captcha sc) {
            success = sc.getCaptcha().equals(captcha);
        }
        else {
            success = false;
        }
        redisTemplate.opsForHash().delete(group, captchaId);
        return success;
    }

    @Override
    public String getCaptchaText(String group, String captchaId) {
        Object storedCaptcha = redisTemplate.opsForValue().get("%s:%s".formatted(group, captchaId));
        if (storedCaptcha instanceof Captcha sc) {
            return sc.getCaptcha();
        }
        else {
            return null;
        }
    }

}
