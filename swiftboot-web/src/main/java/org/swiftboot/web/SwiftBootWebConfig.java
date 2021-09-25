package org.swiftboot.web;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SwiftBoot 配置类
 *
 * @author swiftech
 **/
@Configuration
@EnableConfigurationProperties
@Order(1)
public class SwiftBootWebConfig implements WebMvcConfigurer {

    @Bean
    public SwiftBootWebConfigBean swiftBootWebConfigBean() {
        return new SwiftBootWebConfigBean();
    }

}
