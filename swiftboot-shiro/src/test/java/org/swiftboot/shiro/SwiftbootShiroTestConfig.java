package org.swiftboot.shiro;

import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.context.annotation.*;
import org.swiftboot.data.SwiftBootDataConfig;

/**
 * @author swiftech
 */
@Configuration
@Import({
        AnnotationAwareAspectJAutoProxyCreator.class,
        SwiftBootDataConfig.class, SwiftbootShiroConfig.class
})
public class SwiftbootShiroTestConfig {

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        chainDefinition.addPathDefinition("/unit-test/greeting", "authc");
        chainDefinition.addPathDefinition("/unit-test/admin_only", "authc, perms[admin]");
        chainDefinition.addPathDefinition("/unit-test/staff_only", "authc, perms[staff]");
        chainDefinition.addPathDefinition("/**", "anon");
        return chainDefinition;
    }
}
