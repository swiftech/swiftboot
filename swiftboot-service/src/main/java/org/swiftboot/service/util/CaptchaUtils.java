package org.swiftboot.service.util;

import java.util.Random;

/**
 * @author swiftech 2019-01-18
 **/
public class CaptchaUtils {

    /**
     * 创建随机验证码
     *
     * @return
     */
    public static String makeCaptcha(int length) {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < length; i++) {
            result += random.nextInt(10);
        }
        return result;
    }
}
