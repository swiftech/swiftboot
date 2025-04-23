package org.swiftboot.auth.interceptor;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;
import org.swiftboot.auth.service.SessionService;
import org.swiftboot.common.auth.response.LogoutResponse;

/**
 * Automatically remove user session of current authenticated user from the
 * {@link LogoutResponse} object.
 *
 * @author swiftech
 * @see org.swiftboot.auth.service.SessionService
 * @see LogoutResponse
 * @since 3.0
 */
@ControllerAdvice
@ConditionalOnProperty(value = "swiftboot.auth.authType", havingValue = "session")
public class UserSessionLogoutResponseAdvice extends AbstractMappingJacksonResponseBodyAdvice {

    private static final Logger log = LoggerFactory.getLogger(UserSessionLogoutResponseAdvice.class);

    @Resource
    private SessionService sessionService;

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
        if (StringUtils.isBlank(accessToken)) {
//            ResponseEntity<Object> responseEntity = ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//            String json = new Gson().toJson(responseEntity);
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return;
        }
        // remove session by access token.
        sessionService.removeSession(accessToken);
    }
}
