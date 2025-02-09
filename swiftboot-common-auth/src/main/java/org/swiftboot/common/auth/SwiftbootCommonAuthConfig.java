package org.swiftboot.common.auth;

import jakarta.annotation.Resource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@EnableConfigurationProperties
@Order(2)
public class SwiftbootCommonAuthConfig {

    @Resource
    private JwtConfigBean jwtConfigBean;

    @Bean
    public JwtTokenProvider jwtTokenProvider() {
        return new JwtTokenProvider(jwtConfigBean);
    }

}
