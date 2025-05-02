package org.swiftboot.auth.aop;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import org.swiftboot.auth.config.AuthConfigBean;
import org.swiftboot.auth.controller.BaseAuthenticatedRequest;
import org.swiftboot.auth.model.Session;
import org.swiftboot.auth.service.SessionService;
import org.swiftboot.util.JsonUtils;
import org.swiftboot.web.util.SpringWebUtils;

import jakarta.annotation.Resource;
import java.lang.reflect.Type;

/**
 * Automatically populate the id and name of current authenticated user from session to the request
 * {@link BaseAuthenticatedRequest} object.
 *
 * @author swiftech
 * @see BaseAuthenticatedRequest
 * @since 2.1
 */
@ControllerAdvice
@ConditionalOnProperty(value = "swiftboot.auth.authType", havingValue = "session")
public class UserSessionAdvice extends RequestBodyAdviceAdapter {

    private static final Logger log = LoggerFactory.getLogger(UserSessionAdvice.class);

    @Resource
    private AuthConfigBean configBean;

    @Resource
    private SessionService sessionService;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return targetType == BaseAuthenticatedRequest.class || BaseAuthenticatedRequest.class.isAssignableFrom((Class<?>) targetType);
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (log.isTraceEnabled()) log.trace("SessionAdvice.afterBodyRead()");
        if (log.isTraceEnabled()) log.trace("Handle Request: %s".formatted(body.getClass()));
        BaseAuthenticatedRequest<?> request = (BaseAuthenticatedRequest<?>) body;
        System.out.println(JsonUtils.object2PrettyJson(request));

        String tokenKey = configBean.getTokenKey();

        String token = SpringWebUtils.getHeader(tokenKey, inputMessage);
        if (StringUtils.isBlank(token)) {
            token = SpringWebUtils.getCookieFromHeader(tokenKey, inputMessage);
        }

        if (StringUtils.isBlank(token)) {
            if (log.isTraceEnabled()) log.trace("No token found in headers or cookie");
            return request;
        }
        else {
            Session session = sessionService.getSession(token);
            if (session == null) {
                if (log.isTraceEnabled()) log.trace("No session found for token: %s".formatted(token));
                return request;
            }
            if (log.isDebugEnabled()) log.debug("Find and pre-set user id: %s".formatted(session.getUserId()));
            request.setUserId(session.getUserId());
            request.setUserName(session.getUserName());
        }
        return body;
    }
}
