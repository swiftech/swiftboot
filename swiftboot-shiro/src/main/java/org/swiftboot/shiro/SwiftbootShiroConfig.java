package org.swiftboot.shiro;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
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
import org.swiftboot.shiro.config.SwiftbootShiroConfigBean;
import org.swiftboot.shiro.constant.ShiroSessionStorageType;
import org.swiftboot.shiro.realm.UserAuthorizingRealm;
import org.swiftboot.shiro.service.ShiroSecurityService;
import org.swiftboot.shiro.service.impl.ShiroSecurityServiceImpl;
import org.swiftboot.shiro.session.ShiroSessionListener;
import org.swiftboot.shiro.session.ShiroSessionRedisDao;

import jakarta.annotation.Resource;
import jakarta.servlet.Filter;
import java.util.ArrayList;
import java.util.Map;

/**
 * All required beans that make shiro works.
 *
 * @author swiftech
 * @since 1.2
 **/
@Configuration
@EnableConfigurationProperties
@ConditionalOnProperty(value = "swiftboot.shiro.enabled", havingValue = "true")
public class SwiftbootShiroConfig {

    @Resource
    private SwiftbootShiroConfigBean swiftbootShiroConfigBean;

    @Bean
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
        if (ShiroSessionStorageType.redis == swiftbootShiroConfigBean.getSession().getStorageType()){
            ret.setSessionDAO(shiroSessionRedisDao());
        }
        else {
            ret.setSessionDAO(shiroSessionMemoryDao());
        }
        ret.setGlobalSessionTimeout(swiftbootShiroConfigBean.getSession().getTimeout() * 1000L);
        ret.setSessionIdCookie(sessionIdCookie());
        ret.setSessionListeners(new ArrayList<SessionListener>() {
            {
                add(shiroSessionListener());
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
    @ConditionalOnProperty(value = "swiftboot.shiro.session.storageType", havingValue = "redis")
    public SessionDAO shiroSessionRedisDao() {
        return new ShiroSessionRedisDao();
    }

    @Bean
    @ConditionalOnMissingBean
    public SessionDAO shiroSessionMemoryDao() {
        return new MemorySessionDAO();
    }

    @Bean
    @ConditionalOnMissingBean
    ShiroSecurityService shiroSecurityService() {
        return new ShiroSecurityServiceImpl();
    }

    /**
     * 默认的用户认证域
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    public Realm userAuthorizingRealm() {
        return new UserAuthorizingRealm();
    }

    @Bean
    ShiroSessionListener shiroSessionListener() {
        return new ShiroSessionListener();
    }

    @Bean
    @ConditionalOnMissingBean(ShiroFilterFactoryBean.class)
    public ShiroFilterFactoryBean shiroFilterFactoryBean(ShiroFilterChainDefinition filterChainDefinition, Map<String, Filter> filterMap) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager());
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinition.getFilterChainMap());
        shiroFilterFactoryBean.setFilters(filterMap);
        shiroFilterFactoryBean.setLoginUrl(swiftbootShiroConfigBean.getLoginUrl());
        shiroFilterFactoryBean.setSuccessUrl(swiftbootShiroConfigBean.getSuccessUrl());
        for (String m : filterMap.keySet()) {
            System.out.printf("register filter: %s-%s%n", m, filterMap.get(m));
        }
        return shiroFilterFactoryBean;
    }

    //  以下定义是为了 Shiro 的注解生效问题（否则连接口都不能访问404）
    @Bean
    @DependsOn({"lifecycleBeanPostProcessor"})
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }

//    @Bean
//    @ConditionalOnMissingBean
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
