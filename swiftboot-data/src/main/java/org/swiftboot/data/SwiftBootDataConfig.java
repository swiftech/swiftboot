package org.swiftboot.data;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.swiftboot.data.model.aspect.EntityIdAspect;
import org.swiftboot.data.model.aspect.UpdateTimeAspect;
import org.swiftboot.data.model.id.DefaultIdGenerator;
import org.swiftboot.data.model.id.IdGenerator;
import org.swiftboot.data.model.id.IdPopulator;
import org.swiftboot.data.model.interceptor.IdInterceptor;
import org.swiftboot.data.model.interceptor.InterceptorProxy;
import org.swiftboot.data.model.interceptor.TimeInterceptor;
import org.swiftboot.data.model.interceptor.TimeInterceptorRegisterBean;

/**
 * @author swiftech
 **/
@Configuration
public class SwiftBootDataConfig {

    public static final String DATA_MODEL_AUTO_GENERATE_ID = "swiftboot.data.model.autoGenerateId";
    public static final String DATA_MODEL_AUTO_UPDATE_TIME_STRATEGY = "swiftboot.data.model.autoUpdateTimeStrategy";

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
    @ConditionalOnProperty(value = DATA_MODEL_AUTO_GENERATE_ID, havingValue = "true")
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

    @Bean
    @ConditionalOnBean(IdGenerator.class)
    @ConditionalOnProperty(value = DATA_MODEL_AUTO_GENERATE_ID, havingValue = "true")
    IdPopulator idPopulator() {
        return new IdPopulator();
    }


    @Bean
    @ConditionalOnProperty(value = DATA_MODEL_AUTO_GENERATE_ID, havingValue = "true")
    IdInterceptor idInterceptor() {
        return new IdInterceptor();
    }

    /**
     * Only works when swiftboot.data.model.autoUpdateTimeStrategy=always
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(value = DATA_MODEL_AUTO_UPDATE_TIME_STRATEGY, havingValue = "always")
    UpdateTimeAspect updateTimeAspect() {
        return new UpdateTimeAspect();
    }

    @Bean
    TimeInterceptor timeInterceptor() {
        return new TimeInterceptor();
    }

    @Bean
    InterceptorProxy interceptorProxy() {
        InterceptorProxy interceptorProxy = new InterceptorProxy();
        if (swiftBootDataConfigBean().getModel().isAutoGenerateId()) {
            interceptorProxy.addInterceptor(idInterceptor());
        }
        interceptorProxy.addInterceptor(timeInterceptor());
        return interceptorProxy;
    }

    @Bean
    @ConditionalOnBean(InterceptorProxy.class)
    TimeInterceptorRegisterBean timeInterceptorRegisterBean() {
        return new TimeInterceptorRegisterBean();
    }


//    @Bean
//    @ConditionalOnProperty("swiftboot.web.model.initData")
//    public Initializer initializer() {
//        return new Initializer();
//    }


}
