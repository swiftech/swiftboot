package org.swiftboot.auth.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.swiftboot.auth.filter.JwtAuthFilter;

/**
 * @author swiftech
 * @since 3.0
 **/
@Configuration
@EnableConfigurationProperties
@Order(2)
@ConditionalOnProperty(value = "swiftboot.auth.authType", havingValue = "jwt")
public class SwiftbootAuthJwtConfig {

    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter();
    }


}
