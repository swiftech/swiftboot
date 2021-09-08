package org.swiftboot.auth;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.swiftboot.auth.filter.AuthFilter;
import org.swiftboot.auth.interceptor.UserSessionMessageConverter;
import org.swiftboot.auth.interceptor.UserIdArgumentResolver;
import org.swiftboot.auth.service.SessionService;
import org.swiftboot.auth.service.impl.MockSessionServiceImpl;
import org.swiftboot.auth.service.impl.SessionServiceRedisImpl;
import org.swiftboot.service.service.RedisService;

import java.util.List;

/**
 * @author swiftech 2019-05-24
 **/
@Configuration
@EnableConfigurationProperties
@Order(2)
public class SwiftbootAuthConfig implements WebMvcConfigurer {

    @Bean
    public SwiftbootAuthConfigBean swiftbootAuthConfigBean() {
        return new SwiftbootAuthConfigBean();
    }

    @Bean
    @ConditionalOnProperty(value = "swiftboot.auth.enabled", havingValue = "true")
    public AuthFilter authFilter() {
        return new AuthFilter();
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
    public UserIdArgumentResolver userIdArgumentResolver() {
        return new UserIdArgumentResolver();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userIdArgumentResolver());
    }

    @Bean
    public UserSessionMessageConverter sessionConverter() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        return new UserSessionMessageConverter(builder.build());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(0, sessionConverter());
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//        System.out.println("### extend the session converter: " + converters.size());
//        converters.add(0, sessionConverter());
//        for (HttpMessageConverter<?> converter : converters) {
//            System.out.println("$$$" + converter);
//        }
    }

}
