package org.swiftboot.web;

import org.apache.commons.lang3.SystemUtils;

/**
 * 判断 Spring Boot 的环境
 *
 * @author swiftech
 **/
public class SpringBootEnv {

    public static boolean isProductionMode = false;

    public static boolean isTestMode = false;

    public static boolean isDevMode = true;

    static {
        String profile = SystemUtils.getEnvironmentVariable("spring.profiles.active", "dev");
        if ("prod".equals(profile)) {
            System.out.println("生产模式");
            isProductionMode = true;
            isTestMode = false;
            isDevMode = false;
        }
        else if ("test".equals(profile)) {
            System.out.println("测试模式");
            isProductionMode = false;
            isTestMode = true;
            isDevMode = false;
        }
        else {
            System.out.println("开发模式");
            isProductionMode = false;
            isTestMode = false;
            isDevMode = true;
        }
    }
}
