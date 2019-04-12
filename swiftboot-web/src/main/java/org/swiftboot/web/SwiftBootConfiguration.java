package org.swiftboot.web;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ConfigurableWebEnvironment;

/**
 * SwiftBoot 配置类
 *
 * @author Allen 2019-04-09
 **/
@Configuration
@EnableConfigurationProperties
public class SwiftBootConfiguration {

    @Bean
    public SwiftBootConfigBean swiftBootConfigBean(ConfigurableWebEnvironment env) {
        return new SwiftBootConfigBean();
    }

}
