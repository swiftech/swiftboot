package org.swiftboot.auth.config;

import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.swiftboot.auth.filter.JwtAuthFilter;
import org.swiftboot.auth.interceptor.JwtArgumentResolver;
import org.swiftboot.auth.service.JwtService;
import org.swiftboot.auth.service.impl.InMemoryJwtServiceImpl;
import org.swiftboot.common.auth.JwtConfigBean;

import java.util.List;

/**
 * @author swiftech
 * @since 3.0
 **/
@Configuration
@EnableConfigurationProperties
@Order(2)
@ConditionalOnProperty(value = "swiftboot.auth.authType", havingValue = "jwt")
public class SwiftbootAuthJwtConfig implements WebMvcConfigurer {

    @Resource
    private JwtConfigBean jwtConfigBean;

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter();
    }

    /**
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(JwtService.class)
    public JwtService inMemoryJwtService() {
        return new InMemoryJwtServiceImpl();
    }

    @Bean
    public JwtArgumentResolver jwtArgumentResolver() {
        return new JwtArgumentResolver();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(jwtArgumentResolver());
    }

}
