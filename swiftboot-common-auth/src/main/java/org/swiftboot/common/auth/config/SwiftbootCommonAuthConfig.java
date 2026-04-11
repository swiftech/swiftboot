package org.swiftboot.common.auth.config;

import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.swiftboot.common.auth.JwtService;
import org.swiftboot.common.auth.JwtTokenProvider;
import org.swiftboot.common.auth.aop.JwtArgumentResolver;
import org.swiftboot.common.auth.service.JwtStore;
import org.swiftboot.common.auth.service.impl.InMemoryJwtStore;
import org.swiftboot.common.auth.service.impl.JwtServiceImpl;
import org.swiftboot.common.auth.service.impl.RedisJwtStore;

import java.util.List;

@Configuration
@EnableConfigurationProperties
@Order(2)
public class SwiftbootCommonAuthConfig implements WebMvcConfigurer {

    @Resource
    private JwtConfigBean jwtConfigBean;

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(jwtConfigBean);
    }

    @Bean
    public JwtService jwtService() {
        return new JwtServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
//    @ConditionalOnProperty(value = "swiftboot.auth.jwt.storeMode", havingValue = "memory")
    public JwtStore inMemoryJwtStore() {
        return new InMemoryJwtStore();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "swiftboot.auth.jwt.storeMode", havingValue = "redis")
    public JwtStore databaseJwtStore() {
        return new RedisJwtStore();
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
