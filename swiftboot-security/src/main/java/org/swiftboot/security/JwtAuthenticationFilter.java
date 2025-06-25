package org.swiftboot.security;

import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.swiftboot.common.auth.JwtService;
import org.swiftboot.common.auth.JwtTokenProvider;
import org.swiftboot.common.auth.JwtUtils;
import org.swiftboot.common.auth.config.JwtConfigBean;
import org.swiftboot.common.auth.filter.BaseAuthFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

/**
 * @since 3.0
 */
@Component
public class JwtAuthenticationFilter extends BaseAuthFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Resource
    private JwtTokenProvider jwtProvider;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private JwtConfigBean jwtConfig;

    @Resource
    private JwtService jwtService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("JwtAuthenticationFilter doFilter");
        // Retrieve the Authorization header
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = null;
        String userId = null;
        String username = null;

        try {
            // Check if the header starts with "Bearer "
            token = JwtUtils.extractBearerToken(authHeader);
            if (StringUtils.isBlank(token)) {
                throw new AuthorizationDeniedException("Invalid access token");
            }
            else {
                // NO need to handle 'refresh' type of revocation.
                if (!jwtConfig.isRefreshRevokeType()) {
                    // deny access if the token has been revoked.
                    if (jwtService.isRevokedAccessToken(token)) {
                        log.warn("Revoked jwt token: %s".formatted(StringUtils.abbreviateMiddle(token, "...", 30)));
                        SecurityContextHolder.getContext().setAuthentication(null);
                        // TODO this returns 500 ERROR to client instead of 401
                        throw new AuthorizationDeniedException("Invalid access token");
                    }
                }
                userId = jwtProvider.getUserId(token);
                username = jwtProvider.getUsername(token);
            }

            log.debug("JwtAuthenticationFilter userId: [{}], username: [{}]", userId, username);

            // If the token is valid and no authentication is set in the context
            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                log.debug("User authorities: {}", userDetails.getAuthorities());
                // Validate token and set authentication
                if (jwtProvider.validateToken(token)) {
                    log.debug("Access token is valid, allowed to access");
                    SecurityContext newContext = SecurityContextHolder.createEmptyContext();
                    Authentication authenticationToken = new UserIdAuthenticationToken(userId, userDetails.getAuthorities());
                    newContext.setAuthentication(authenticationToken);
                    SecurityContextHolder.setContext(newContext);
                }
                else {
                    log.warn("Access Token is invalid");
                    throw new AuthorizationDeniedException("Invalid access token");
                }
            }
            // Continue the filter chain
            filterChain.doFilter(request, response);
        } catch (AccessDeniedException | AuthorizationDeniedException e) {
            log.error(e.getMessage(), e);
            super.responseWithHttpStatus(response, HttpStatus.UNAUTHORIZED.value(), e.getLocalizedMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            super.responseWithHttpStatus(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getLocalizedMessage());
        }
    }
}