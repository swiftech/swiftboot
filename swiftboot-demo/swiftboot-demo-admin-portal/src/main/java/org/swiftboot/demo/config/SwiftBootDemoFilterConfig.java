package org.swiftboot.demo.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.swiftboot.demo.filter.SwiftbootDemoFilter;
//import org.swiftboot.auth.filter.AuthFilter;


/**
 * @author swiftech
 */
@Configuration
@ConditionalOnProperty(value = "swiftboot.auth.enabled", havingValue = "true")
public class SwiftBootDemoFilterConfig implements WebMvcConfigurer {

//    @Resource
//    AuthFilter authFilter;
//
//    @Bean
//    public FilterRegistrationBean<AuthFilter> regAuthFilter() {
//        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(authFilter);
//        registrationBean.addUrlPatterns("/goods/*");
//        registrationBean.addUrlPatterns("/order/*");
//        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
//        return registrationBean;
//    }

    @Bean
    public SwiftbootDemoFilter swiftbootDemoFilter() {
        return new SwiftbootDemoFilter();
    }

    @Bean
    public FilterRegistrationBean<SwiftbootDemoFilter> regDemoFilter(SwiftbootDemoFilter filter) {
        FilterRegistrationBean<SwiftbootDemoFilter> registrationBean = new FilterRegistrationBean<>(filter);
//        registrationBean.setEnabled(false);
        registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
        return registrationBean;
    }


}
