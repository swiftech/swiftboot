package org.swiftboot.security;

import io.jsonwebtoken.JwtException;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.swiftboot.common.auth.JwtConfigBean;
import org.swiftboot.common.auth.JwtTokenProvider;
import org.swiftboot.common.auth.JwtUtils;

import java.io.IOException;

/**
 * @since 3.0.0
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Resource
    private JwtTokenProvider jwtService;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private RevokedTokenDao revokedTokenDao;

    @Resource
    private JwtConfigBean jwtConfig;

//    @Value("{swiftboot.auth.jwt.revokeType}")
//    private boolean directRevoke;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("JwtAuthenticationFilter doFilter");
        // Retrieve the Authorization header
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        try {
            // Check if the header starts with "Bearer "
            if (authHeader != null) {


                token = JwtUtils.extractBearerToken(authHeader);
                if (token != null) {
                    // NO need to handle 'refresh' type of revocation.
                    if (!jwtConfig.isRefreshRevokeType()) {
                        // deny access if token has been revoked.
                        if (revokedTokenDao.isRevoked(token)) {
                            log.warn("Revoked jwt token: %s".formatted(StringUtils.abbreviateMiddle(token, "...", 30)));
                            SecurityContextHolder.getContext().setAuthentication(null);
                            filterChain.doFilter(request, response);
                            return;
                        }
                    }
                    username = jwtService.getUsername(token); // Extract username from token
                }
            }

            // If the token is valid and no authentication is set in the context
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);


                // Validate token and set authentication
                if (jwtService.validateToken(token)) {

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

            }
        } catch (JwtException e) {
            log.error(e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}