package org.swiftboot.common.auth.aop;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.swiftboot.common.auth.request.UserContextHolder;

/**
 * Cleanup the user context after one request is completed.
 * @since 3.2
 */
@Component
public class UserContextCleanupInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(UserContextCleanupInterceptor.class);

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        log.debug("Cleanup user context");
        UserContextHolder.removeClientSource();
    }
}
