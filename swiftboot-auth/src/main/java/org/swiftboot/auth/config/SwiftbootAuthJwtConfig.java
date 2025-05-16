package org.swiftboot.auth.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.swiftboot.auth.filter.JwtAuthFilter;
import org.swiftboot.auth.service.UserAuthService;
import org.swiftboot.auth.service.impl.DefaultUserJwtAuthService;
import org.swiftboot.common.auth.token.JwtAuthentication;

/**
 * @author swiftech
 * @since 3.0
 **/
@Configuration
@EnableConfigurationProperties
@ConditionalOnProperty(value = "swiftboot.auth.authType", havingValue = "jwt")
public class SwiftbootAuthJwtConfig {

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter();
    }

    /**
     * Default UserAuthService if no customized one provided.
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(UserAuthService.class)
    public UserAuthService<JwtAuthentication> userAuthService() {
        return new DefaultUserJwtAuthService<>();
    }

}
