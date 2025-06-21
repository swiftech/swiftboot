package org.swiftboot.auth.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.swiftboot.auth.aop.UserSessionArgumentResolver;
import org.swiftboot.auth.filter.SessionAuthFilter;
import org.swiftboot.auth.model.Session;
import org.swiftboot.auth.service.SessionService;
import org.swiftboot.auth.service.UserAuthService;
import org.swiftboot.auth.service.impl.DefaultUserSessionAuthService;
import org.swiftboot.auth.service.impl.MockSessionService;
import org.swiftboot.auth.service.impl.RedisSessionService;

import java.util.List;

/**
 * @author swiftech 2019-05-24
 **/
@Configuration
@ConditionalOnProperty(value = "swiftboot.auth.authType", havingValue = "session")
public class SwiftbootAuthSessionConfig implements WebMvcConfigurer {

    @Bean
    public SessionAuthFilter authFilter() {
        return new SessionAuthFilter();
    }

    /**
     * Default UserAuthService if no customized one provided.
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(UserAuthService.class)
    public UserAuthService<Session> userAuthService() {
        return new DefaultUserSessionAuthService<>();
    }

    /**
     * 只有配置为 redis 存储会话才会加载
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(value = "swiftboot.auth.session.type", havingValue = "redis")
    public SessionService redisSessionService() {
        return new RedisSessionService();
    }

    /**
     * 默认的会话管理
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SessionService.class)
    public SessionService mockSessionService() {
        return new MockSessionService();
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
