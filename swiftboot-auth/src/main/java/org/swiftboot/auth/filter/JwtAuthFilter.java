package org.swiftboot.auth.filter;

import io.jsonwebtoken.JwtException;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.swiftboot.common.auth.JwtTokenProvider;
import org.swiftboot.common.auth.aop.TokenHelper;
import org.swiftboot.common.auth.filter.BaseAuthFilter;
import org.swiftboot.web.i18n.MessageHelper;

import java.io.IOException;

/**
 * Validate JWT token from client, if invalid, 401 Unauthorized response returns.
 *
 * @since 3.0.0
 */
public class JwtAuthFilter extends BaseAuthFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    @Resource
    @Qualifier("swiftbootAuthMessageSource")
    private MessageSource swiftbootAuthMessageSource;

    @Resource
    private TokenHelper tokenHelper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (log.isDebugEnabled())
            log.debug("do auth check for: [%s]%s".formatted(request.getMethod(), request.getRequestURI()));
        // TODO: Workaround for loading locale from client header 'Accept-Language'
        LocaleContextHolder.setLocale(request.getLocale());
        log.debug(String.valueOf(request.getLocale()));
        // Get JWT token from HTTP request
        String token = tokenHelper.getTokenFromRequest(request);
        if (log.isDebugEnabled())
            log.debug("Check access token: " + org.apache.commons.lang3.StringUtils.abbreviateMiddle(token, "...", 30));
        // Validate Token
        try {
            if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
                // get username from token
                String userId = jwtTokenProvider.getUserId(token);
                log.info("userId = {}", userId);
                filterChain.doFilter(request, response);
            }
            else {
                if (log.isWarnEnabled()) log.warn("User does not have a valid token");
                super.responseWithHttpStatus(response, HttpStatus.UNAUTHORIZED.value(), MessageHelper.getMessage(swiftbootAuthMessageSource, "swiftboot.auth.invalid.access.token"));
            }
        } catch (JwtException | IOException e) {
            log.error(e.getMessage(), e);
            super.responseWithHttpStatus(response, HttpStatus.UNAUTHORIZED.value(), MessageHelper.getMessage(swiftbootAuthMessageSource, "swiftboot.auth.invalid.access.token"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            super.responseWithHttpStatus(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), MessageHelper.getMessage(swiftbootAuthMessageSource, "swiftboot.auth.internal.server.error"));
        }
    }

}
