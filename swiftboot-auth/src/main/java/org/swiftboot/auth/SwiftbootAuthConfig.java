package org.swiftboot.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.swiftboot.auth.filter.AuthFilter;
import org.swiftboot.auth.service.SessionService;
import org.swiftboot.auth.service.impl.MockSessionServiceImpl;
import org.swiftboot.auth.service.impl.SessionServiceRedisImpl;
import org.swiftboot.service.service.RedisService;

/**
 *
 * @author swiftech 2019-05-24
 **/
@Configuration
@EnableConfigurationProperties
public class SwiftbootAuthConfig {
    private Logger log = LoggerFactory.getLogger(SwiftbootAuthConfig.class);

    @Bean
    public SwiftbootAuthConfigBean swiftbootAuthConfigBean() {
        return new SwiftbootAuthConfigBean();
    }

    @Bean
    @ConditionalOnProperty(value = "swiftboot.auth.enabled", havingValue = "true")
    public AuthFilter securityFilter() {
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
     * 模拟用户会话，仅用于开发（dev）模式
     *
     * @return
     */
    @Bean
    @ConditionalOnBean(RedisService.class)
    @ConditionalOnProperty(value = "swiftboot.auth.session.type", havingValue = "mock")
    public SessionService mockSessionService() {
        return new MockSessionServiceImpl();
    }

}
