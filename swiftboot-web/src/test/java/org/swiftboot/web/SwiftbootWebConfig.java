package org.swiftboot.web;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.swiftboot.data.model.id.EntityIdGenerator;
import org.swiftboot.data.model.id.IdGenerator;

/**
 * @author swiftech
 **/
//@Configuration
//@TestConfiguration
//@EnableWebMvc
@ComponentScan(basePackages = {
        "org.swiftboot.web"
})
@EnableJpaRepositories(basePackages = {
        "org.swiftboot.web.model.dao"
})
@EntityScan(basePackages = {
        "org.swiftboot.web.model.entity"
})
public class SwiftbootWebConfig {
    @Bean
    IdGenerator idGenerator() {
        return new EntityIdGenerator();
    }

}
