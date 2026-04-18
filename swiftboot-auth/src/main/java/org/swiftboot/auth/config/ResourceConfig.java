package org.swiftboot.auth.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class ResourceConfig {


    @Bean(name = "swiftbootAuthMessageSource")
    public MessageSource swiftbootAuthMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n/swiftboot-auth");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
