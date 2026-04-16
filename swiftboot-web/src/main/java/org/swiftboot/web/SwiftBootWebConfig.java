package org.swiftboot.web;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SwiftBoot-Web 配置类
 *
 * @author swiftech
 **/
@Configuration
@EnableConfigurationProperties
@Order(1)
public class SwiftBootWebConfig implements WebMvcConfigurer {

    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(swiftbootWebMessageSource());
        return validator;
    }

    @Bean(name = "swiftbootWebMessageSource")
    public MessageSource swiftbootWebMessageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("i18n/swiftboot-web", "i18n/validation");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}
