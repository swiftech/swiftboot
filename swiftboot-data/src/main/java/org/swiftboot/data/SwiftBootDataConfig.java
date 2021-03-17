package org.swiftboot.data;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.swiftboot.data.constant.AutoUpdateTimeStrategy;
import org.swiftboot.data.model.aspect.EntityIdAspect;
import org.swiftboot.data.model.aspect.UpdateTimeAspect;
import org.swiftboot.data.model.id.DefaultIdGenerator;
import org.swiftboot.data.model.id.IdGenerator;
import org.swiftboot.data.model.id.IdPopulator;
import org.swiftboot.data.model.interceptor.*;

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
    @ConditionalOnProperty(value = DATA_MODEL_AUTO_UPDATE_TIME_STRATEGY, havingValue = AutoUpdateTimeStrategy.AUTO_UPDATE_TIME_ALWAYS)
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
        if (!AutoUpdateTimeStrategy.AUTO_UPDATE_TIME_NOT_SET.equals(swiftBootDataConfigBean().getModel().getAutoUpdateTimeStrategy())) {
            interceptorProxy.addInterceptor(timeInterceptor());
        }
        return interceptorProxy;
    }

    @Bean
    @ConditionalOnBean(InterceptorProxy.class)
    InterceptorLoader interceptorHandler() {
        return new InterceptorLoader();
    }

    @Bean
    @ConditionalOnBean(InterceptorLoader.class)
    InterceptorProxyRegisterBean interceptorProxyRegisterBean() {
        return new InterceptorProxyRegisterBean();
    }


//    @Bean
//    @ConditionalOnProperty("swiftboot.web.model.initData")
//    public Initializer initializer() {
//        return new Initializer();
//    }


}
