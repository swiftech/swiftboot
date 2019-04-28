package org.swiftboot.web.aspect;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Allen 2019-04-28
 **/
@Configuration
public class AspectTestConfig {

    @Bean
    AspectTarget aspectTarget() {
        return new AspectTarget();
    }

    @Bean
    MyAspect myAspect() {
        return new MyAspect();
    }
}
