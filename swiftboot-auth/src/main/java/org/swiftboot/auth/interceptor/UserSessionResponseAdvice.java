package org.swiftboot.auth.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;
import org.swiftboot.auth.config.SwiftbootAuthConfigBean;
import org.swiftboot.auth.controller.AuthenticatedResponse;
import org.swiftboot.auth.service.Session;
import org.swiftboot.auth.service.SessionService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;

/**
 * Automatically save session of current authenticated user from the
 * {@link AuthenticatedResponse} object and write the user token to Cookie (if enabled).
 *
 * @author swiftech
 * @see org.swiftboot.auth.service.UserAuthService
 * @see AuthenticatedResponse
 * @since 2.2
 */
@ControllerAdvice
public class UserSessionResponseAdvice extends AbstractMappingJacksonResponseBodyAdvice {

    private static final Logger log = LoggerFactory.getLogger(UserSessionResponseAdvice.class);

    @Resource
    private SwiftbootAuthConfigBean authConfigBean;

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
        AuthenticatedResponse<?> authenticatedResponse = (AuthenticatedResponse<?>) bodyContainer.getValue();
        Session userSession = authenticatedResponse.getUserSession();
        if (userSession == null) return;
        String userToken = userSession.getUserToken();
        if (StringUtils.isBlank(userToken)) return;

        // cache the session
        sessionService.addSession(userToken, userSession);

        // return cookie to client
        ServletServerHttpResponse servletResponse = (ServletServerHttpResponse) response;
        if (authConfigBean.getSession().isUseCookie()) {
            Cookie cookie = new Cookie(authConfigBean.getTokenKey(), userToken);
            cookie.setPath(authConfigBean.getSession().getCookiePath());
            int expiresIn = authConfigBean.getSession().getExpiresIn();
            cookie.setMaxAge(expiresIn == 0 ? Integer.MAX_VALUE : expiresIn);
            servletResponse.getServletResponse().addCookie(cookie);
            log.debug("Response with cookie %s".formatted(authConfigBean.getTokenKey()));
        }
        else {
            servletResponse.getServletResponse().setHeader(authConfigBean.getTokenKey(), userToken);
            log.debug("Response with header %s".formatted(authConfigBean.getTokenKey()));
        }
    }
}
