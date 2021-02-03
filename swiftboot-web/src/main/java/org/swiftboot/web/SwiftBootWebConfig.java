package org.swiftboot.web;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.swiftboot.web.command.MessageConverter;

import java.util.List;

/**
 * SwiftBoot 配置类
 *
 * @author swiftech
 **/
@Configuration
@EnableConfigurationProperties
public class SwiftBootWebConfig implements WebMvcConfigurer {

    @Bean
    public SwiftBootWebConfigBean swiftBootConfigBean() {
        return new SwiftBootWebConfigBean();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        converters.add(new MessageConverter(builder.build()));
    }

    // 测试环境不工作（ConfigurableWebEnvironment = null)
//    @Bean
//    public SwiftBootConfigBean swiftBootConfigBean(ConfigurableWebEnvironment env) {
//        return new SwiftBootConfigBean();
//    }
}
