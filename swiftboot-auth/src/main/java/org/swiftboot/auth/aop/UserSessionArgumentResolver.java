package org.swiftboot.auth.aop;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.swiftboot.auth.config.AuthConfigBean;
import org.swiftboot.common.auth.annotation.*;
import org.swiftboot.auth.filter.SessionAuthFilter;
import org.swiftboot.auth.model.Session;
import org.swiftboot.auth.service.SessionService;

import jakarta.annotation.Resource;

import java.util.Map;

/**
 * Populate values from session to the annotated parameter of controller.
 * {@link UserId}, {@link UserName}, {@link ExpireTime}, {@link Addition}, {@link org.swiftboot.auth.annotation.Session}
 * this argument resolver only works under Session mode.
 *
 * @author swiftech
 * @see SessionAuthFilter
 * @since 2.1
 */
public class UserSessionArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Logger log = LoggerFactory.getLogger(UserSessionArgumentResolver.class);

    @Resource
    private AuthConfigBean configBean;

    @Resource
    private SessionService sessionService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Token.class)
                || parameter.hasParameterAnnotation(UserId.class)
                || parameter.hasParameterAnnotation(UserName.class)
                || parameter.hasParameterAnnotation(ExpireTime.class)
                || parameter.hasParameterAnnotation(Addition.class)
                || parameter.hasParameterAnnotation(org.swiftboot.auth.annotation.Session.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        ServletWebRequest servletWebRequest = (ServletWebRequest) webRequest;
        // Try to get token from headers, if the token is in Cookie, it must have been extracted from Cookie in previous AuthFilter
        String token = servletWebRequest.getHeader(configBean.getTokenKey());
        if (StringUtils.isBlank(token)) {
            if (log.isTraceEnabled()) log.trace("No token found in headers");
            return null;
        }
        else {
            Session session = sessionService.getSession(token);
            if (session == null) {
                if (log.isTraceEnabled()) log.trace("No session found in for token: %s".formatted(token));
                return null;
            }
            if (log.isInfoEnabled()) log.info("Find and pre-set user id: %s".formatted(session.getUserId()));
            if (parameter.hasParameterAnnotation(Token.class)) {
                return token;
            }
            else if (parameter.hasParameterAnnotation(UserId.class)) {
                return session.getUserId();
            }
            else if (parameter.hasParameterAnnotation(UserName.class)) {
                return session.getUserName();
            }
            else if (parameter.hasParameterAnnotation(ExpireTime.class)) {
                return session.getExpireTime();
            }
            else if (parameter.hasParameterAnnotation(Addition.class)) {
                Addition anno = parameter.getParameterAnnotation(Addition.class);
                if (anno != null && StringUtils.isNotBlank(anno.value())) {
                    Map<String, Object> additions = session.getAdditions();
                    if (additions != null && additions.containsKey(anno.value())) {
                        return additions.get(anno.value());
                    }
                }
                return null;
            }
            else if (parameter.hasParameterAnnotation(org.swiftboot.auth.annotation.Session.class)) {
                return session;
            }
            else {
                return null;
            }
        }
    }
}
