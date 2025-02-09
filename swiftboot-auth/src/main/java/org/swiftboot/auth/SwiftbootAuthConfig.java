package org.swiftboot.auth;

import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.swiftboot.auth.filter.AuthFilter;
import org.swiftboot.auth.filter.JwtAuthFilter;
import org.swiftboot.auth.interceptor.UserSessionArgumentResolver;
import org.swiftboot.auth.service.SessionService;
import org.swiftboot.auth.service.impl.MockSessionServiceImpl;
import org.swiftboot.auth.service.impl.SessionServiceRedisImpl;
import org.swiftboot.common.auth.JwtConfigBean;
import org.swiftboot.service.service.RedisService;

import java.util.List;

/**
 * @author swiftech 2019-05-24
 **/
@Configuration
@EnableConfigurationProperties
@Order(2)
public class SwiftbootAuthConfig implements WebMvcConfigurer {

    @Resource
    private JwtConfigBean jwtConfigBean;

    @Bean
    @ConditionalOnProperty(value = "swiftboot.auth.authType", havingValue = "session")
    public AuthFilter authFilter() {
        return new AuthFilter();
    }

    @Bean
    @ConditionalOnProperty(value = "swiftboot.auth.authType", havingValue = "jwt")
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter();
    }

    /**
     * 只有配置为 redis 存储会话才会加载
     *
     * @return
     */
    @Bean
    @ConditionalOnBean(RedisService.class)
    @ConditionalOnProperty(value = "swiftboot.auth.session.type", havingValue = "redis")
    public SessionService redisSessionService() {
        return new SessionServiceRedisImpl();
    }

    /**
     * 默认的会话管理
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SessionService.class)
    public SessionService mockSessionService() {
        return new MockSessionServiceImpl();
    }

    @Bean
    public UserSessionArgumentResolver userSessionArgumentResolver() {
        return new UserSessionArgumentResolver();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userSessionArgumentResolver());
    }

}
