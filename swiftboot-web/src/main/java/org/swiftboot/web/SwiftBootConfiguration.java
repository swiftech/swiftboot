package org.swiftboot.web;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.swiftboot.web.command.MessageConverter;
import org.swiftboot.web.model.Initializer;

import java.util.List;

/**
 * SwiftBoot 配置类
 *
 * @author swiftech
 **/
@Configuration
@EnableConfigurationProperties
public class SwiftBootConfiguration implements WebMvcConfigurer {

    @Bean
    public SwiftBootConfigBean swiftBootConfigBean() {
        return new SwiftBootConfigBean();
    }

//    @Bean
//    @ConditionalOnProperty("swiftboot.web.model.initData")
//    public Initializer initializer() {
//        return new Initializer();
//    }

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
