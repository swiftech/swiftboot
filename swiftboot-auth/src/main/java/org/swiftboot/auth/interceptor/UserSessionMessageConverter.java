package org.swiftboot.auth.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.swiftboot.auth.config.SwiftbootAuthConfigBean;
import org.swiftboot.auth.controller.BaseAuthenticatedCommand;
import org.swiftboot.auth.service.Session;
import org.swiftboot.auth.service.SessionService;
import org.swiftboot.web.command.WebMessageConverter;
import org.swiftboot.web.util.SpringWebUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Automatically populate the id and name of current authenticated user from session to the request
 * {@link org.swiftboot.auth.controller.BaseAuthenticatedCommand} object.
 *
 * @author swiftech
 * @since 2.1
 * @see org.swiftboot.auth.controller.BaseAuthenticatedCommand
 * @deprecated
 */
public class UserSessionMessageConverter extends WebMessageConverter {

    private final Logger log = LoggerFactory.getLogger(UserSessionMessageConverter.class);

    @Resource
    private SwiftbootAuthConfigBean configBean;

    @Resource
    private SessionService sessionService;

    public UserSessionMessageConverter() {
    }

    public UserSessionMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        Object converted = super.read(type, contextClass, inputMessage);
        if (converted instanceof BaseAuthenticatedCommand) {
            BaseAuthenticatedCommand<?> command = (BaseAuthenticatedCommand<?>) converted;
            String tokenKey = configBean.getSession().getTokenKey();

            String token = SpringWebUtils.getHeader(tokenKey, inputMessage);
            if (StringUtils.isBlank(token)) {
                token = SpringWebUtils.getCookieFromHeader(tokenKey, inputMessage);
            }

            if (StringUtils.isBlank(token)) {
                log.trace("No token found in headers or cookie");
                return converted;
            }
            else {
                Session session = sessionService.getSession(token);
                if (session == null) {
                    log.trace("No session found for token: " + token);
                    return converted;
                }
                log.info("Find and pre-set user id: " + session.getUserId());
                command.setUserId(session.getUserId());
                command.setUserName(session.getUserName());
            }
        }
        return converted;
    }
}
