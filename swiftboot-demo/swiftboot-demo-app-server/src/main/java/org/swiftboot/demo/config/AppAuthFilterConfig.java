package org.swiftboot.demo.config;

import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.swiftboot.auth.filter.AuthFilter;
import org.swiftboot.auth.service.UserAuthService;
import org.swiftboot.demo.service.AppUserAuthServiceImpl;

/**
 *
 * @author swiftech
 * @since 3.0.0
 */
@Configuration
@ConditionalOnProperty(value = "swiftboot.auth.authType", havingValue = "session")
public class AppAuthFilterConfig implements WebMvcConfigurer {

    @Resource
    AuthFilter authFilter;

    @Bean
    public FilterRegistrationBean<AuthFilter> regAuthFilter() {
        FilterRegistrationBean<AuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(authFilter);
        registrationBean.addUrlPatterns("/app/secure");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        return registrationBean;
    }

    @Bean
    @ConditionalOnProperty(value = "swiftboot.auth.authType", havingValue = "session")
    public UserAuthService userAuthService() {
        return new AppUserAuthServiceImpl();
    }

}
