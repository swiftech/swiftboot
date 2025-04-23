package org.swiftboot.common.auth.aop;

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
import org.swiftboot.common.auth.JwtService;
import org.swiftboot.common.auth.response.LogoutResponse;

/**
 * Automatically revoke access token and refresh token of current authenticated user from the
 * {@link LogoutResponse} object.
 *
 * @author swiftech
 * @see JwtService
 * @see LogoutResponse
 * @since 3.0
 */
@ControllerAdvice
@ConditionalOnProperty(value = "swiftboot.auth.authType", havingValue = "jwt")
public class JwtLogoutResponseAdvice extends AbstractMappingJacksonResponseBodyAdvice {

    private static final Logger log = LoggerFactory.getLogger(JwtLogoutResponseAdvice.class);

    @Resource
    private JwtService jwtService;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return super.supports(returnType, converterType)
                && LogoutResponse.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType, MethodParameter returnType,
                                           ServerHttpRequest request, ServerHttpResponse response) {
        if (log.isDebugEnabled()) log.debug("Handle user tokens after logout.");
        LogoutResponse<?> logoutResponse = (LogoutResponse<?>) bodyContainer.getValue();
        String accessToken = logoutResponse.getAccessToken();
        if (StringUtils.isBlank(accessToken)) return;
        // revoke authenticate by access token.
        jwtService.revokeAuthenticationByAccessToken(accessToken);
    }
}
