package org.swiftboot.web.aop;

import jakarta.annotation.Resource;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.swiftboot.web.config.SwiftBootWebConfigBean;

/**
 * Force all APIs to wait for a long time for testing the frontend timeout processing.
 * It's only for testing, DO NOT use for production.
 */
@Aspect
@Component
@ConditionalOnProperty(name = "swiftboot.web.mock.mockTimeout", havingValue = "true")
public class GlobalApiDelayAspect {

    private static final Logger log = LoggerFactory.getLogger(GlobalApiDelayAspect.class);

    @Resource
    private SwiftBootWebConfigBean swiftBootWebConfigBean;

    /**
     * Match all restfull endpoints.
     *
     * @throws InterruptedException
     */
    @Before("within(@org.springframework.stereotype.Controller *) || within(@org.springframework.web.bind.annotation.RestController *)")
    public void mockTimeout() throws InterruptedException {
        log.debug("Mocking timeout in %d milliseconds".formatted(swiftBootWebConfigBean.getMock().getTimeout()));
        Thread.sleep(swiftBootWebConfigBean.getMock().getTimeout());
    }
}
