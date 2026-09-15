package org.swiftboot.web;

import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.swiftboot.web.i18n.LocaleContextDecorator;

/**
 * @since 3.2
 */
@Configuration
public class AutoContextAsyncConfig {

    @Bean
    public TaskDecorator taskDecorator() {
        return new LocaleContextDecorator();
    }

    /**
     * 利用 Spring Boot 自动注入的 builder
     * 它已经包含了 application.yml 里配置的所有线程池参数！
     */
    @Bean(name = "taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor(
            ThreadPoolTaskExecutorBuilder builder,
            TaskDecorator localeTaskDecorator) {

        // 基于 YAML 里的默认参数构建，仅叠加 TaskDecorator
        return builder.taskDecorator(localeTaskDecorator).build();
    }
}
