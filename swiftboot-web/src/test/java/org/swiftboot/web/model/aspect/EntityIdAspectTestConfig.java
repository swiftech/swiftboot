package org.swiftboot.web.model.aspect;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.swiftboot.web.SwiftBootModelConfiguration;
import org.swiftboot.web.model.id.EntityIdGenerator;
import org.swiftboot.web.model.id.IdGenerator;

/**
 * @author Allen 2019-04-28
 **/
@Configuration
@Import(SwiftBootModelConfiguration.class)
public class EntityIdAspectTestConfig {

    @Bean
    IdGenerator idGenerator() {
        return new EntityIdGenerator();
    }
}
