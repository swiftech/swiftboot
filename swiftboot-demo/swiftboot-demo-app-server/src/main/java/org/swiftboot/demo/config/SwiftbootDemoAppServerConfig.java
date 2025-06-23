package org.swiftboot.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
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
        "org.swiftboot.common.auth",
        "org.swiftboot.demo"
})
// 和org.swiftboot.demo在同一个包下面的可以不配置
@EntityScan(basePackages = {
        "org.swiftboot.demo.model",
        "org.swiftboot.service.model",
})
@EnableJpaRepositories(basePackages = {
        "org.swiftboot.auth.repository",
        "org.swiftboot.demo.repository",
})
public class SwiftbootDemoAppServerConfig {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:messages", "classpath:message", "classpath:validation", "classpath:error_message");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false); // not using system default locale.
        return messageSource;
    }

    @Bean
    public OpenAPI swiftBootDemoAppServerOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("SwiftBoot Demo App Server API")
                        .description("SwiftBoot Demo App Server API with simple authentication")
                        .version("v3.0.0")
                        .license(new License().name("Apache 2.0").url("https://github.com/swiftech/swiftboot")));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("API")
                .pathsToMatch(
                        "/app/**",
                        "/health/**"
                )
                .build();
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
