package org.swiftboot.demo.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.swiftboot.data.model.Initializer;
import org.swiftboot.data.model.id.EntityIdGenerator;
import org.swiftboot.data.model.id.IdGenerator;

/**
 * @author swiftech
 **/
@Configuration
@EnableAutoConfiguration
@EnableWebMvc
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = {
        "org.swiftboot.web",
        "org.swiftboot.data",
        "org.swiftboot.service",
        "org.swiftboot.auth",
//        "org.swiftboot.shiro",
        "org.swiftboot.demo"
})
// 和org.swiftboot.demo在同一个包下面的可以不配置
@EntityScan(basePackages = {
        "org.swiftboot.demo.model.entity",
        "org.swiftboot.service.model.entity",
})
@EnableJpaRepositories(basePackages = {
        "org.swiftboot.demo.model.dao",
//        "org.swiftboot.demo.shiro",
})
public class SwiftbootDemoAppServerConfig {

    @Bean
    public IdGenerator idGenerator() {
        return new EntityIdGenerator();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:message", "classpath:validation", "classpath:error_message");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public Initializer initializer() {
        Initializer initializer = new Initializer();
//        initializer.forEntities(GoodsEntity.class, GoodsDetailEntity.class);
        return initializer;
    }

//    @Bean
//    DeleteInterceptor deleteInterceptor(){
//        return new DeleteInterceptor();
//    }
//
//    @Bean
//    InterceptorRegisterBean<DeleteInterceptor> registerDeleteInterceptor() {
//        InterceptorRegisterBean<DeleteInterceptor> regBean = new InterceptorRegisterBean<>();
//        regBean.setInterceptor(deleteInterceptor());
//        return regBean;
//    }
//
//    @Bean
//    DataPermissionInterceptor dataPermissionInterceptor() {
//        return new DataPermissionInterceptor();
//    }
//
//    @Bean
//    InterceptorRegisterBean<DataPermissionInterceptor> registerDataPermissionInterceptor() {
//        InterceptorRegisterBean<DataPermissionInterceptor> regBean = new InterceptorRegisterBean<>();
//        regBean.setOrder(100);
//        regBean.setInterceptorName("My Data Interceptor");
//        regBean.setInterceptor(dataPermissionInterceptor());
//        return regBean;
//    }

}
