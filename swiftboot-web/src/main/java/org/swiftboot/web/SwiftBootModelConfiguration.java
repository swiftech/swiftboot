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
 * @author swiftech
 **/
@Configuration
public class SwiftBootModelConfiguration {

    /**
     * 如果 swiftboot.web.model.autoGenerateId=true 自动加载实体类 ID 切面
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(value = "swiftboot.web.model.autoGenerateId", havingValue = "true")
    EntityIdAspect entityIdAspect() {
        return new EntityIdAspect();
    }

    /**
     * 加载默认的 ID 生成器，采用 UUID 生成主键ID
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(IdGenerator.class)
    IdGenerator defaultIdGenerator() {
        return new DefaultIdGenerator();
    }

    /**
     * 根据 swiftboot.web.model 加载实体类更新时间的切面
     * @return
     */
    @Bean
    @ConditionalOnProperty(value = "swiftboot.web.model")
    UpdateTimeAspect updateTimeAspect() {
        return new UpdateTimeAspect();
    }


}
