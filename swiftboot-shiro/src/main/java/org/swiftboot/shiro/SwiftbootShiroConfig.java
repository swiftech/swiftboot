package org.swiftboot.shiro;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.swiftboot.service.service.RedisService;
import org.swiftboot.shiro.realm.UserAuthorizingRealm;
import org.swiftboot.shiro.service.PasswordManager;
import org.swiftboot.shiro.service.ShiroSecurityService;
import org.swiftboot.shiro.service.impl.DefaultPasswordManager;
import org.swiftboot.shiro.service.impl.ShiroSecurityServiceImpl;
import org.swiftboot.shiro.session.ShiroSessionListener;
import org.swiftboot.shiro.session.ShiroSessionRedisDao;

import javax.annotation.Resource;
import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author swiftech
 * @since 1.2
 **/
@Configuration
@EnableConfigurationProperties
public class SwiftbootShiroConfig {

    @Resource
    private SwiftbootShiroConfigBean swiftbootShiroConfigBean;

    @Bean
    @ConditionalOnMissingBean(PasswordManager.class)
    public PasswordManager passwordManager() {
        return new DefaultPasswordManager();
    }

    @Bean
    @ConditionalOnBean(DefaultWebSecurityManager.class)
    AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(defaultWebSecurityManager());
        return advisor;
    }

    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealms(new ArrayList<Realm>() {
            {
                add(userAuthorizingRealm());
            }
        });
        manager.setSessionManager(defaultWebSessionManager());
        return manager;
    }

    @Bean
    public DefaultWebSessionManager defaultWebSessionManager() {
        DefaultWebSessionManager ret = new DefaultWebSessionManager();
        ret.setSessionIdCookieEnabled(true);
        ret.setSessionIdUrlRewritingEnabled(false);
        ret.setSessionDAO(shiroSessionRedisDao());
        ret.setGlobalSessionTimeout(swiftbootShiroConfigBean.getSession().getTimeout() * 1000);
        ret.setSessionIdCookie(sessionIdCookie());
        ret.setSessionListeners(new ArrayList<SessionListener>(){
            {
                add( shiroSessionListener());
            }
        });
        return ret;
    }


    @Bean
    @ConditionalOnProperty("swiftboot.shiro.cookie.name")
    public SimpleCookie sessionIdCookie() {
        SimpleCookie bean = new SimpleCookie();
        bean.setDomain(swiftbootShiroConfigBean.getCookie().getDomain());
        bean.setPath(swiftbootShiroConfigBean.getCookie().getPath());
        bean.setName(swiftbootShiroConfigBean.getCookie().getName());
        bean.setMaxAge(swiftbootShiroConfigBean.getCookie().getMaxAge());
        return bean;
    }

    @Bean
    @ConditionalOnBean(RedisService.class)
    public ShiroSessionRedisDao shiroSessionRedisDao() {
        return new ShiroSessionRedisDao();
    }

    @Bean
//    @ConditionalOnBean(UserAuthorizingRealm.class)
    ShiroSecurityService shiroSecurityService() {
        return new ShiroSecurityServiceImpl();
    }

    /**
     * 默认的用户认证域
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(Realm.class)
    public UserAuthorizingRealm userAuthorizingRealm() {
        return new UserAuthorizingRealm();
    }

    @Bean
    ShiroSessionListener shiroSessionListener() {
        return new ShiroSessionListener();
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(ShiroFilterChainDefinition filterChainDefinition, Map<String, Filter> filterMap) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager());
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinition.getFilterChainMap());
        shiroFilterFactoryBean.setFilters(filterMap);
        shiroFilterFactoryBean.setLoginUrl("/login");
        return shiroFilterFactoryBean;
    }

    //  以下定义是为了 Shiro 的注解生效问题（否则连接口都不能访问404）
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

//    @Bean
//    LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
//        return new LifecycleBeanPostProcessor();
//    }
//
//    @Bean
//    @ConditionalOnBean(LifecycleBeanPostProcessor.class)
//    DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
//        return new DefaultAdvisorAutoProxyCreator();
//    }
}
