package org.swiftboot.demo;

import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置所有 URL 的访问权限
 *
 * @author swiftech
 * @since 1.2
 */
@Configuration
public class SwiftbootDemoShiroConfig {

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        chainDefinition.addPathDefinition("/admin/user/**", "authc, perms[admin:user]");

        // Module Goods
        chainDefinition.addPathDefinition("/goods/create/**", "authc, perms[goods:create]");
        chainDefinition.addPathDefinition("/goods/delete/**", "authc, perms[goods:delete]");
        chainDefinition.addPathDefinition("/goods/**", "authc, perms[goods]");

        // Module Order
        chainDefinition.addPathDefinition("/order/**", "authc, perms[order]");

        // No authentication and permissions required
        chainDefinition.addPathDefinition("/**", "anon");
        return chainDefinition;
    }
}
