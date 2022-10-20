package org.swiftboot.auth.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import org.swiftboot.auth.config.SwiftbootAuthConfigBean;
import org.swiftboot.auth.controller.BaseAuthenticatedCommand;
import org.swiftboot.auth.service.Session;
import org.swiftboot.auth.service.SessionService;
import org.swiftboot.util.JsonUtils;
import org.swiftboot.web.util.SpringWebUtils;

import javax.annotation.Resource;
import java.lang.reflect.Type;

/**
 * Automatically populate the id and name of current authenticated user from session to the request
 * {@link org.swiftboot.auth.controller.BaseAuthenticatedCommand} object.
 *
 * @author swiftech
 * @see org.swiftboot.auth.controller.BaseAuthenticatedCommand
 * @since 2.1
 */
@ControllerAdvice
public class UserSessionAdvice extends RequestBodyAdviceAdapter {

    private final Logger log = LoggerFactory.getLogger(UserSessionAdvice.class);

    @Resource
    private SwiftbootAuthConfigBean configBean;

    @Resource
    private SessionService sessionService;

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return targetType == BaseAuthenticatedCommand.class || BaseAuthenticatedCommand.class.isAssignableFrom((Class<?>) targetType);
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (log.isTraceEnabled()) log.trace("SessionAdvice.afterBodyRead()");
        if (log.isTraceEnabled()) log.trace("Handle command: " + body.getClass());
        BaseAuthenticatedCommand<?> command = (BaseAuthenticatedCommand<?>) body;
        System.out.println(JsonUtils.object2PrettyJson(command));

        String tokenKey = configBean.getSession().getTokenKey();

        String token = SpringWebUtils.getHeader(tokenKey, inputMessage);
        if (StringUtils.isBlank(token)) {
            token = SpringWebUtils.getCookieFromHeader(tokenKey, inputMessage);
        }

        if (StringUtils.isBlank(token)) {
            if (log.isTraceEnabled()) log.trace("No token found in headers or cookie");
            return command;
        }
        else {
            Session session = sessionService.getSession(token);
            if (session == null) {
                if (log.isTraceEnabled()) log.trace("No session found for token: " + token);
                return command;
            }
            if (log.isDebugEnabled()) log.debug("Find and pre-set user id: " + session.getUserId());
            command.setUserId(session.getUserId());
            command.setUserName(session.getUserName());
        }
        return body;
    }
}
