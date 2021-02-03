package org.swiftboot.data;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.swiftboot.data.model.aspect.EntityIdAspect;
import org.swiftboot.data.model.aspect.UpdateTimeAspect;
import org.swiftboot.data.model.id.DefaultIdGenerator;
import org.swiftboot.data.model.id.IdGenerator;

/**
 * @author swiftech
 **/
@Configuration
public class SwiftBootDataConfig {

    @Bean
    public SwiftBootDataConfigBean swiftBootDataConfigBean() {
        return new SwiftBootDataConfigBean();
    }

    /**
     * 如果 swiftboot.data.model.autoGenerateId=true 自动加载实体类 ID 切面
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(value = "swiftboot.data.model.autoGenerateId", havingValue = "true")
    EntityIdAspect entityIdAspect() {
        return new EntityIdAspect();
    }

    /**
     * 加载默认的 ID 生成器，采用 UUID 生成主键ID
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(IdGenerator.class)
    IdGenerator<?> defaultIdGenerator() {
        return new DefaultIdGenerator();
    }

    /**
     * 根据 swiftboot.data.model.autoUpdateTime=true 加载实体类更新时间的切面
     * @return
     */
    @Bean
    @ConditionalOnProperty(value = "swiftboot.data.model.autoUpdateTime", havingValue = "true")
    UpdateTimeAspect updateTimeAspect() {
        return new UpdateTimeAspect();
    }


//    @Bean
//    @ConditionalOnProperty("swiftboot.web.model.initData")
//    public Initializer initializer() {
//        return new Initializer();
//    }


}
