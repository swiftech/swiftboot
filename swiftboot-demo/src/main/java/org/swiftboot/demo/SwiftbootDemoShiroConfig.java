package org.swiftboot.demo;

import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author swiftech
 * @since 1.2
 */
@Configuration
public class SwiftbootDemoShiroConfig {

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        chainDefinition.addPathDefinition("/goods/delete/**", "authc, perms[goods:delete]");
        chainDefinition.addPathDefinition("/order/**", "authc");
        chainDefinition.addPathDefinition("/**", "anon");
        return chainDefinition;
    }
}
