package org.swiftboot.auth.aop;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;
import org.swiftboot.auth.config.AuthConfigBean;
import org.swiftboot.auth.config.SessionConfigBean;
import org.swiftboot.common.auth.response.AuthenticatedResponse;
import org.swiftboot.auth.model.Session;
import org.swiftboot.auth.service.SessionService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;

/**
 * Automatically save session of current authenticated user from the
 * {@link AuthenticatedResponse} object and write the user token to Cookie (if enabled) or the header of HTTP response.
 *
 * @author swiftech
 * @see org.swiftboot.auth.service.UserAuthService
 * @see AuthenticatedResponse
 * @since 2.2
 */
@ControllerAdvice
@ConditionalOnProperty(value = "swiftboot.auth.authType", havingValue = "session")
public class UserSessionResponseAdvice extends AbstractMappingJacksonResponseBodyAdvice {

    private static final Logger log = LoggerFactory.getLogger(UserSessionResponseAdvice.class);

    @Resource
    private AuthConfigBean authConfig;

    @Resource
    private SessionConfigBean sessionConfig;

    @Resource
    private SessionService sessionService;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return super.supports(returnType, converterType)
                && AuthenticatedResponse.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType, MethodParameter returnType,
                                           ServerHttpRequest request, ServerHttpResponse response) {
        if (log.isDebugEnabled()) log.debug("Handle user session and cookie after authenticated.");
        AuthenticatedResponse<?, ?> authenticatedResponse = (AuthenticatedResponse<?, ?>) bodyContainer.getValue();
        Object session = authenticatedResponse.getAuthenticated();
        if (session == null) return;
        if (session instanceof Session userSession) {
            String userToken = userSession.getUserToken();
            if (StringUtils.isBlank(userToken)) return;

            // cache the session
            sessionService.addSession(userToken, userSession);

            // return cookie to client
            ServletServerHttpResponse servletResponse = (ServletServerHttpResponse) response;
            if (sessionConfig.isUseCookie()) {
                Cookie cookie = new Cookie(authConfig.getTokenKey(), userToken);
                cookie.setPath(sessionConfig.getCookiePath());
                int expiresIn = sessionConfig.getExpiresIn();
                cookie.setMaxAge(expiresIn == 0 ? Integer.MAX_VALUE : expiresIn);
                servletResponse.getServletResponse().addCookie(cookie);
                if (log.isDebugEnabled()) log.debug("Response with cookie %s".formatted(authConfig.getTokenKey()));
            }
            else {
                servletResponse.getServletResponse().setHeader(authConfig.getTokenKey(), userToken);
                if (log.isDebugEnabled()) log.debug("Response with header %s".formatted(authConfig.getTokenKey()));
            }
        }

    }
}
