package org.swiftboot.auth.interceptor;

import jakarta.annotation.Resource;
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
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;
import org.swiftboot.auth.config.AuthConfigBean;
import org.swiftboot.common.auth.token.JwtAuthentication;
import org.swiftboot.auth.service.AuthenticatedResponse;
import org.swiftboot.common.auth.JwtService;
import org.swiftboot.common.auth.token.AccessToken;

/**
 * Automatically save access token and refresh token of current authenticated user from the
 * {@link AuthenticatedResponse} object.
 *
 * @author swiftech
 * @see JwtService
 * @see AuthenticatedResponse
 * @since 3.0
 */
@ControllerAdvice
@ConditionalOnProperty(value = "swiftboot.auth.authType", havingValue = "jwt")
public class UserJwtResponseAdvice extends AbstractMappingJacksonResponseBodyAdvice {

    private static final Logger log = LoggerFactory.getLogger(UserJwtResponseAdvice.class);

    @Resource
    private AuthConfigBean authConfigBean;

    @Resource
    private JwtService jwtService;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return super.supports(returnType, converterType)
                && AuthenticatedResponse.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType, MethodParameter returnType,
                                           ServerHttpRequest request, ServerHttpResponse response) {
        if (log.isDebugEnabled()) log.debug("Handle user tokens after authenticated.");
        AuthenticatedResponse<?, ?> authenticatedResponse = (AuthenticatedResponse<?, ?>) bodyContainer.getValue();
        Object authenticated = authenticatedResponse.getAuthenticated();
        if (authenticated == null) return;
        if (authenticated instanceof JwtAuthentication ja) {
            AccessToken accessToken = ja.getAccessToken();
            if (StringUtils.isBlank(accessToken.tokenValue())) return;

            // save authenticated dual token.
            jwtService.saveJwtAuthentication(ja);
        }
    }
}
