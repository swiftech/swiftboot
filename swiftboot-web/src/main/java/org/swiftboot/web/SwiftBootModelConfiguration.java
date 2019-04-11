package org.swiftboot.web;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.swiftboot.web.model.aspect.EntityIdAspect;
import org.swiftboot.web.model.aspect.UpdateTimeAspect;

/**
 * @author Allen 2019-04-09
 **/
@Configuration
@ConditionalOnProperty(value = "swiftboot.web.model.autoGenerateId")
public class SwiftBootModelConfiguration {

    @Bean
    EntityIdAspect entityIdAspect() {
        return new EntityIdAspect();
    }

    @Bean
    UpdateTimeAspect updateTimeAspect() {
        return new UpdateTimeAspect();
    }
}
