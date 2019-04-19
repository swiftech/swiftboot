package org.swiftboot.web;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.swiftboot.web.model.aspect.EntityIdAspect;
import org.swiftboot.web.model.aspect.UpdateTimeAspect;
import org.swiftboot.web.model.id.DefaultIdGenerator;
import org.swiftboot.web.model.id.IdGenerator;

/**
 * @author Allen 2019-04-09
 **/
@Configuration
public class SwiftBootModelConfiguration {

    @Bean
    @ConditionalOnProperty(value = "swiftboot.web.model.autoGenerateId", havingValue = "true")
    EntityIdAspect entityIdAspect() {
        return new EntityIdAspect();
    }

    @Bean
    UpdateTimeAspect updateTimeAspect() {
        return new UpdateTimeAspect();
    }

    @Bean
    @ConditionalOnMissingBean(IdGenerator.class)
    IdGenerator defaultIdGenerator() {
        return new DefaultIdGenerator();
    }
}
