package org.swiftboot.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.swiftboot.demo.dao.MockRefreshTokenDaoImpl;
import org.swiftboot.demo.dao.MockRevokedTokenDaoImpl;
import org.swiftboot.security.RefreshTokenDao;
import org.swiftboot.security.RevokedTokenDao;

/**
 * @since 3.0.0
 */
@Configuration
public class AuthenticationConfig {

    @Bean
    public RevokedTokenDao revokedTokenDaoStub() {
        return new MockRevokedTokenDaoImpl();
    }

    @Bean
    public RefreshTokenDao refreshTokenDaoStub() {
        return new MockRefreshTokenDaoImpl();
    }

}
