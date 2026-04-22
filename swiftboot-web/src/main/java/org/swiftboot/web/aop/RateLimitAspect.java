package org.swiftboot.web.aop;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.swiftboot.util.NetworkUtils;
import org.swiftboot.web.annotation.RateLimit;
import org.swiftboot.web.constant.LimitType;
import org.swiftboot.web.i18n.MessageHelper;
import org.swiftboot.web.util.HttpServletUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Limit the HTTP request traffic for each endpoint or for each user on each endpoint，
 * Enable this functionality by adding {@code RateLimit} annotation to the controller's methods that you want to limit the request,
 * set window time and a limit on the number of HTTP requests.
 * The expired requests in cache will be cleared every 60 minutes.
 *
 * @since 3.1.1
 * @see RateLimit
 */
@Aspect
@Component
public class RateLimitAspect {

    private static final Logger log = LoggerFactory.getLogger(RateLimitAspect.class);

    @Resource
    @Qualifier("swiftbootWebMessageSource")
    private MessageSource swiftbootWebMessageSource;

    // limit cache：Key -> Counter
    private final Map<String, RequestCounter> limitMap = new ConcurrentHashMap<>();

    public RateLimitAspect() {
        // clean the cache
        try (ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor()) {
            executor.scheduleAtFixedRate(() -> {
                log.info("Clean rate limit cache");
                long now = System.currentTimeMillis();
                limitMap.entrySet().removeIf(entry -> now > entry.getValue().expireTime);
            }, 60, 60, TimeUnit.MINUTES);
        }
    }

    @Around("@annotation(rateLimit)")
    public Object interceptor(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return joinPoint.proceed(); // not web application
        }

        String key = joinPoint.getSignature().toShortString();
        if (rateLimit.limitType() == LimitType.USER){
            HttpServletRequest request = attributes.getRequest();
            String userId = getUserIdentifier(request);
            key = "%s:%s".formatted(key, userId);
        }

        log.debug("Lock key: %s".formatted(key));
        int count = rateLimit.count();
        long timeInMillis = rateLimit.time();

        long now = System.currentTimeMillis();
        RequestCounter counter = limitMap.computeIfAbsent(key, k -> new RequestCounter(now + timeInMillis));

        // Current time window expired, reset counter.
        if (now > counter.expireTime) {
            counter.count.set(1);
            counter.expireTime = now + timeInMillis;
        } else {
            // check count in current time window.
            if (counter.count.getAndIncrement() > count) {
                throw new RuntimeException(MessageHelper.getMessage(swiftbootWebMessageSource, "swiftboot.web.rate.limit.cooling"));
            }
        }
        return joinPoint.proceed();
    }

    /**
     * User identifier from http request.
     */
    private String getUserIdentifier(HttpServletRequest request) {
        // 1. try user access Token
        String token = request.getHeader("Authorization");
        if (token != null && !token.isEmpty()) return token;

        // 2. try Session ID
        String sessionId = request.getRequestedSessionId();
        if (sessionId != null) return sessionId;

        // 3. try IP from reverse proxy
        String realIp = HttpServletUtils.getClientIp(request);
        if (StringUtils.isNotBlank(realIp) && !NetworkUtils.isLocalOrPrivateIp(realIp)) return realIp;

        // 4 fallback (IP address from reverse proxy might be incorrect)
        return request.getRemoteAddr() + request.getHeader("User-Agent");
    }

    private static class RequestCounter {
        AtomicInteger count = new AtomicInteger(1);
        long expireTime;

        RequestCounter(long expireTime) {
            this.expireTime = expireTime;
        }
    }
}
