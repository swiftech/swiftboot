package org.swiftboot.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.swiftboot.demo.dao.MockRefreshTokenService;
import org.swiftboot.demo.dao.MockRevokedTokenServiceImpl;
import org.swiftboot.security.RefreshTokenService;
import org.swiftboot.security.RevokedTokenService;

/**
 * @since 3.0.0
 */
@Configuration
public class AuthenticationConfig {

//    @Bean
//    public RevokedTokenService revokedTokenDaoStub() {
//        return new MockRevokedTokenServiceImpl();
//    }
//
//    @Bean
//    public RefreshTokenService refreshTokenDaoStub() {
//        return new MockRefreshTokenService();
//    }

}
