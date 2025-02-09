package org.swiftboot.auth.filter;

import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;
import org.swiftboot.common.auth.JwtTokenProvider;
import org.swiftboot.web.exception.ErrorCodeSupport;
import io.jsonwebtoken.JwtException;

import java.io.IOException;

/**
 * @since 3.0.0
 */
@Order(Ordered.LOWEST_PRECEDENCE)
public class JwtAuthFilter extends BaseAuthFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Get JWT token from HTTP request
        String token = getTokenFromRequest(request);
        // Validate Token

        try {
            if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
                // get username from token
                String username = jwtTokenProvider.getUsername(token);
                log.info("username = {}", username);
                filterChain.doFilter(request, response);
            }
            else {
                log.warn("User does not have a valid token");
                super.responseWithError(response, ErrorCodeSupport.CODE_NO_SIGNIN);
            }
        } catch (JwtException e) {
            log.error(e.getMessage(), e);
            super.responseWithError(response, ErrorCodeSupport.CODE_NO_SIGNIN);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            super.responseWithError(response, ErrorCodeSupport.CODE_NO_SIGNIN);
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
