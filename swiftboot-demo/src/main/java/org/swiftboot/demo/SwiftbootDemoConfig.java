package org.swiftboot.demo;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.swiftboot.demo.filter.SwiftbootDemoFilter;
import org.swiftboot.web.filter.CorsFilter;
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
        "org.swiftboot.demo",
        "org.swiftboot.web"
})
public class SwiftbootDemoConfig {

    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter();
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> regCorsFilter(CorsFilter filter) {
        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>(filter);
//        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    public SwiftbootDemoFilter swiftbootDemoFilter() {
        return new SwiftbootDemoFilter();
    }

    @Bean
    public FilterRegistrationBean<SwiftbootDemoFilter> regTenantContextFilter(SwiftbootDemoFilter filter) {
        FilterRegistrationBean<SwiftbootDemoFilter> registrationBean = new FilterRegistrationBean<>(filter);
//        registrationBean.setEnabled(false);
        return registrationBean;
    }

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
