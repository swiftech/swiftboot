package org.swiftboot.demo;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.swiftboot.web.model.id.EntityIdGenerator;
import org.swiftboot.web.model.id.IdGenerator;

/**
 * @author swiftech
 **/
@Configuration
@EnableAutoConfiguration
@EnableWebMvc
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = {
        "org.swiftboot.web",
        "org.swiftboot.service",
        "org.swiftboot.auth",
        "org.swiftboot.shiro",
        "org.swiftboot.demo"
})
// 和org.swiftboot.demo在同一个包下面的可以不配置
@EntityScan(basePackages = {
        "org.swiftboot.demo.model.entity",
        "org.swiftboot.service.model.entity",
})
@EnableJpaRepositories(basePackages = {
        "org.swiftboot.demo.model.dao",
        "org.swiftboot.demo.shiro",
        "org.swiftboot.service.model.dao",
})
public class SwiftbootDemoConfig {

    @Bean
    public IdGenerator idGenerator() {
        return new EntityIdGenerator();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:message");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

//
//    @Bean
//    ExceptionProcessor exceptionProcessor() {
//        return new ExceptionProcessor();
//    }
//
//    @Bean
//    ValidationExceptionProcessor validationExceptionProcessor() {
//        return new ValidationExceptionProcessor();
//    }
}
