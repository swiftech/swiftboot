package org.swiftboot.web;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.ConfigurableWebEnvironment;

/**
 * @author Allen 2019-04-09
 **/
@Configuration
@EnableConfigurationProperties
public class SwiftBootConfiguration {

    @Bean
    public SwiftBootConfigBean swiftBootConfigBean(ConfigurableWebEnvironment env) {
        return new SwiftBootConfigBean();
    }

//    @Bean
//    ValidationResultConfigBean validationResultConfigBean(ConfigurableWebEnvironment env) {
//        return new ValidationResultConfigBean();
//    }
}
