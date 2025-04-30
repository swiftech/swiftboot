package org.swiftboot.demo.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.swiftboot.auth.filter.JwtAuthFilter;

import jakarta.annotation.Resource;
import org.swiftboot.auth.service.UserAuthService;
import org.swiftboot.demo.service.AppUserJwtAuthService;

/**
 * Enabled when swiftboot.auth.authType = jwt
 *
 * @author swiftech
 * @since 3.0.0
 */
@Configuration
@ConditionalOnProperty(value = "swiftboot.auth.authType", havingValue = "jwt")
public class AppUserAuthJwtConfig implements WebMvcConfigurer {

    @Resource
    JwtAuthFilter jwtAuthFilter;

    @Bean
    public FilterRegistrationBean<JwtAuthFilter> registerJwtAuthFilter() {
        FilterRegistrationBean<JwtAuthFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtAuthFilter);
        registrationBean.addUrlPatterns("/app/secure");
        registrationBean.addUrlPatterns("/app/data");
        registrationBean.addUrlPatterns("/app/logout");
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        return registrationBean;
    }

}
