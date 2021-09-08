package org.swiftboot.web;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.swiftboot.web.command.WebMessageConverter;

import java.util.List;

/**
 * SwiftBoot 配置类
 *
 * @author swiftech
 **/
@Configuration
@EnableConfigurationProperties
@Order(1)
public class SwiftBootWebConfig implements WebMvcConfigurer {

    @Bean
    public SwiftBootWebConfigBean swiftBootWebConfigBean() {
        return new SwiftBootWebConfigBean();
    }

    @Bean
    public WebMessageConverter messageConverter() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        return new WebMessageConverter(builder.build());
    }


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(0, messageConverter());
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
//        System.out.println("### extend the message converter: " + converters.size());
//        converters.add(0, messageConverter());
//        for (HttpMessageConverter<?> converter : converters) {
//            System.out.println("$$$" + converter);
//        }
    }

    // 测试环境不工作（ConfigurableWebEnvironment = null)
//    @Bean
//    public SwiftBootConfigBean swiftBootConfigBean(ConfigurableWebEnvironment env) {
//        return new SwiftBootConfigBean();
//    }
}
