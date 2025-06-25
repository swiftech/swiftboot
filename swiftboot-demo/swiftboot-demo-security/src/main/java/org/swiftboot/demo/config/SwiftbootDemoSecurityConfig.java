package org.swiftboot.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {
        "org.swiftboot.web",
        "org.swiftboot.data",
        "org.swiftboot.service",
        "org.swiftboot.security",
        "org.swiftboot.security.config",
        "org.swiftboot.common",
        "org.swiftboot.demo"
})
public class SwiftbootDemoSecurityConfig {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:message", "classpath:validation");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setFallbackToSystemLocale(false); // not using system default locale.
        return messageSource;
    }

    @Bean
    public OpenAPI swiftBootDemoAppServerOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("SwiftBoot Demo Security API")
                        .description("SwiftBoot Demo Security Server API with Spring Security authentication&authorization")
                        .version("v3.0.0")
                        .license(new License().name("Apache 2.0").url("https://github.com/swiftech/swiftboot")));
    }
}
