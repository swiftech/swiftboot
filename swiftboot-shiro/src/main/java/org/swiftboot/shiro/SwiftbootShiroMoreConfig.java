package org.swiftboot.shiro;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.swiftboot.shiro.service.PasswordManager;
import org.swiftboot.shiro.service.impl.DefaultPasswordManager;

/**
 * Beans that may be replaced with user defined beans.
 *
 * @author swiftech
 */
@Configuration
@ConditionalOnProperty(value = "swiftboot.shiro.enabled", havingValue = "true")
public class SwiftbootShiroMoreConfig {

    @Bean
    @ConditionalOnMissingBean(PasswordManager.class)
    public PasswordManager passwordManager() {
        return new DefaultPasswordManager();
    }

}
