package org.swiftboot.auth.filter;

import io.jsonwebtoken.JwtException;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.swiftboot.common.auth.JwtTokenProvider;
import org.swiftboot.common.auth.filter.BaseAuthFilter;

import java.io.IOException;

/**
 * @since 3.0.0
 */
public class JwtAuthFilter extends BaseAuthFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (log.isDebugEnabled()) log.debug("do auth check for: %s".formatted(request.getRequestURI()));
        // Get JWT token from HTTP request
        String token = super.getTokenFromRequest(request);
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
                super.responseWithHttpStatus(response, HttpStatus.UNAUTHORIZED.value(), "Invalid access token");
            }
        } catch (JwtException | IOException e) {
            log.error(e.getMessage(), e);
            super.responseWithHttpStatus(response, HttpStatus.UNAUTHORIZED.value(), "Invalid access token");
        }
    }

}
