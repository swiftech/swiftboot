package org.swiftboot.demo.config;

import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.swiftboot.auth.filter.SessionAuthFilter;
import org.swiftboot.auth.service.UserAuthService;
import org.swiftboot.demo.service.AppUserAuthService;

/**
 * Enabled when swiftboot.auth.authType = session
 *
 * @author swiftech
 * @since 3.0.0
 */
@Configuration
@ConditionalOnProperty(value = "swiftboot.auth.authType", havingValue = "session")
public class AppUserAuthSessionConfig implements WebMvcConfigurer {

    @Resource
    SessionAuthFilter sessionAuthFilter;

    @Bean
    public FilterRegistrationBean<SessionAuthFilter> regAuthFilter() {
        FilterRegistrationBean<SessionAuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(sessionAuthFilter);
        registrationBean.addUrlPatterns("/app/secure");
        registrationBean.addUrlPatterns("/app/data");
        registrationBean.addUrlPatterns("/app/logout");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        return registrationBean;
    }
}
