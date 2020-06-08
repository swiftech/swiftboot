package org.swiftboot.shiro;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.swiftboot.shiro.service.PasswordManager;
import org.swiftboot.shiro.service.impl.DefaultPasswordManager;

/**
 * @author swiftech
 */
@Configuration
@EnableConfigurationProperties
public class SwiftbootShiroMoreConfig {

    @Bean
    @ConditionalOnMissingBean(PasswordManager.class)
    public PasswordManager passwordManager() {
        return new DefaultPasswordManager();
    }

}
