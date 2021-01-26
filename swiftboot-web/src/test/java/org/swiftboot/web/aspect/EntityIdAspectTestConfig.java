package org.swiftboot.web.aspect;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.swiftboot.data.SwiftBootDataConfig;
import org.swiftboot.data.model.id.EntityIdGenerator;
import org.swiftboot.data.model.id.IdGenerator;

/**
 * @author swiftech
 **/
@Configuration
@Import({
        SwiftBootDataConfig.class
})
public class EntityIdAspectTestConfig {

    @Bean
    IdGenerator idGenerator() {
        return new EntityIdGenerator();
    }
}
