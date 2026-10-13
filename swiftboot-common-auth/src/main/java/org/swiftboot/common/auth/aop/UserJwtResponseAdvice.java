package org.swiftboot.common.auth.aop;

import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
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
import org.swiftboot.common.auth.config.CookieConfigBean;
import org.swiftboot.common.auth.config.JwtConfigBean;
import org.swiftboot.common.auth.request.UserContextHolder;
import org.swiftboot.common.auth.response.AuthenticatedResponse;
import org.swiftboot.common.auth.service.JwtService;
import org.swiftboot.common.auth.token.AccessToken;
import org.swiftboot.common.auth.token.JwtAuthentication;

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
    private JwtService jwtService;

    @Resource
    private JwtConfigBean jwtConfigBean;

    @Resource
    private TokenHelper tokenHelper;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return super.supports(returnType, converterType)
                && AuthenticatedResponse.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType, MethodParameter returnType,
                                           ServerHttpRequest request, ServerHttpResponse response) {
        if (log.isDebugEnabled())
            log.debug("Handle user tokens after authenticated for client %s".formatted(UserContextHolder.getClientSource()));
        AuthenticatedResponse<?, ?> authenticatedResponse = (AuthenticatedResponse<?, ?>) bodyContainer.getValue();
        Object authenticated = authenticatedResponse.getAuthenticated();
        if (authenticated == null) {
            log.warn("Not authenticated.");
            return;
        }
        if (authenticated instanceof JwtAuthentication ja) {
            AccessToken accessToken = ja.getAccessToken();
            if (StringUtils.isBlank(accessToken.getTokenValue())) {
                log.warn("No access token found");
                return;
            }

            // avoid handling refreshing access token in `immutable` refresh mode since no new refresh token is generated.
            if (ja.getRefreshToken() != null) {
                // save authenticated tokens for refreshing token.
                jwtService.saveJwtAuthentication(ja);
            }

            if (jwtConfigBean.getUseCookie()) {
                CookieConfigBean accessTokenCookieConfig = jwtConfigBean.determineAccessTokenCookie(UserContextHolder.getClientSource());
                if (accessTokenCookieConfig != null) {
                    log.debug("Use cookie for access token. " + accessTokenCookieConfig);
                    Cookie accessTokenCookie = tokenHelper.createTokenCookie(accessTokenCookieConfig, "accessToken", ja.getAccessToken().getTokenValue());
                    log.debug("Cookie domain: %s".formatted(accessTokenCookie.getDomain()));
                    ((ServletServerHttpResponse) response).getServletResponse().addCookie(accessTokenCookie);
                }

                CookieConfigBean refreshTokenCookieConfig = jwtConfigBean.determineRefreshTokenCookie(UserContextHolder.getClientSource());
                if (refreshTokenCookieConfig != null && ja.getRefreshToken() != null) {
                    log.debug("Use cookie for refresh token. " + refreshTokenCookieConfig);
                    Cookie refreshTokenCookie = tokenHelper.createTokenCookie(refreshTokenCookieConfig, "refreshToken", ja.getRefreshToken().getTokenValue());
                    log.debug("Cookie domain: %s".formatted(refreshTokenCookie.getDomain()));
                    ((ServletServerHttpResponse) response).getServletResponse().addCookie(refreshTokenCookie);
                }
            }
            else {
                log.debug("Using cookie is disabled.");
            }
        }
    }
}
