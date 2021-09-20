package org.swiftboot.web;

import org.apache.commons.lang3.SystemUtils;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * 判断 Spring Boot 的环境
 *
 * @author swiftech
 **/
public class SpringBootEnv implements ApplicationListener<ApplicationEvent> {

    public static boolean isProductionMode = false;

    public static boolean isTestMode = false;

    public static boolean isDevMode = true;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationStartingEvent) {

        }
        else if (event instanceof ApplicationEnvironmentPreparedEvent) {

        }
        else if (event instanceof ApplicationReadyEvent) {
            String profile = SystemUtils.getEnvironmentVariable("spring.profiles.active", "default");
            if ("prod".equals(profile)) {
                System.out.println(Info.get(SpringBootEnv.class, R.PRODUCTION));
                isProductionMode = true;
                isTestMode = false;
                isDevMode = false;
            }
            else if ("test".equals(profile)) {
                System.out.println(Info.get(SpringBootEnv.class, R.TESTING));
                isProductionMode = false;
                isTestMode = true;
                isDevMode = false;
            }
            else {
                System.out.println(Info.get(SpringBootEnv.class, R.DEVELOPMENT));
                isProductionMode = false;
                isTestMode = false;
                isDevMode = true;
            }
        }


    }
}
