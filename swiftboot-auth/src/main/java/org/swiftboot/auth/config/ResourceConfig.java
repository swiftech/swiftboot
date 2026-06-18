package org.swiftboot.auth.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class ResourceConfig {


    @Bean(name = "swiftbootAuthMessageSource")
    public MessageSource swiftbootAuthMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/swiftboot-auth");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
