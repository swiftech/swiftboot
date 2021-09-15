package org.swiftboot.auth.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.swiftboot.auth.SwiftbootAuthConfigBean;
import org.swiftboot.auth.service.Session;
import org.swiftboot.auth.service.SessionService;

import javax.annotation.Resource;

/**
 * @author swiftech
 * @see org.swiftboot.auth.filter.AuthFilter
 * @since 2.1
 */
public class UserIdArgumentResolver implements HandlerMethodArgumentResolver {

    private final Logger log = LoggerFactory.getLogger(UserIdArgumentResolver.class);

    @Resource
    private SwiftbootAuthConfigBean configBean;

    @Resource
    private SessionService sessionService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserId.class)
                || parameter.hasParameterAnnotation(UserName.class)
                || parameter.hasParameterAnnotation(org.swiftboot.auth.interceptor.Session.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        ServletWebRequest servletWebRequest = (ServletWebRequest) webRequest;
        // Try to get token from headers, if the token is in Cookie, it must have been extracted from Cookie in previous AuthFilter
        String token = servletWebRequest.getHeader(configBean.getSession().getTokenKey());
        if (StringUtils.isBlank(token)) {
            log.trace("No token found in headers");
            return null;
        }
        else {
            Session session = sessionService.getSession(token);
            if (session == null) {
                log.trace("No session found in for token: " + token);
                return null;
            }
            log.info("Find and pre-set user id: " + session.getUserId());
            if (parameter.hasParameterAnnotation(UserId.class)) {
                return session.getUserId();
            }
            else if (parameter.hasParameterAnnotation(UserName.class)) {
                return session.getUserName();
            }
            else if (parameter.hasParameterAnnotation(org.swiftboot.auth.interceptor.Session.class)) {
                return session;
            }
            else {
                return null;
            }
        }
    }
}
