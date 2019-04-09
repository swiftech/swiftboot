package org.swiftboot.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.swiftboot.web.model.id.EntityIdGenerator;
import org.swiftboot.web.model.id.IdGenerator;

/**
 * @author swiftech
 **/
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {
        "org.swiftboot.demo",
        "org.swiftboot.web.model.aspect",
        "org.swiftboot.web"
})
public class SwiftbootDemoConfig {

    @Bean
    IdGenerator idGenerator() {
        return new EntityIdGenerator();
    }


//
//    @Bean
//    ExceptionProcessor exceptionProcessor() {
//        return new ExceptionProcessor();
//    }
//
//    @Bean
//    ValidationExceptionProcessor validationExceptionProcessor() {
//        return new ValidationExceptionProcessor();
//    }
}
