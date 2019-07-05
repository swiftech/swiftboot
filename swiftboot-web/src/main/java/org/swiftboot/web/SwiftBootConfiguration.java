package org.swiftboot.web;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SwiftBoot 配置类
 *
 * @author swiftech
 **/
@Configuration
@EnableConfigurationProperties
public class SwiftBootConfiguration {


    @Bean
    public SwiftBootConfigBean swiftBootConfigBean() {
        return new SwiftBootConfigBean();
    }

// 测试环境不工作（ConfigurableWebEnvironment = null)
//    @Bean
//    public SwiftBootConfigBean swiftBootConfigBean(ConfigurableWebEnvironment env) {
//        return new SwiftBootConfigBean();
//    }
}
