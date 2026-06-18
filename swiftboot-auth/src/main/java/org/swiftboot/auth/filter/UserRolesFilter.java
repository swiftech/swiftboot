package org.swiftboot.auth.filter;

import io.jsonwebtoken.JwtException;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.swiftboot.common.auth.JwtTokenProvider;
import org.swiftboot.common.auth.filter.BaseAuthFilter;
import org.swiftboot.web.i18n.MessageHelper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Allow users with specific roles to access.
 *
 * @since 3.1.5
 */
public class UserRolesFilter extends BaseAuthFilter {

    private static final Logger log = LoggerFactory.getLogger(UserRolesFilter.class);

    /**
     * Users can access if they have any of the roles.
     */
    private List<String> roles;

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    @Resource
    @Qualifier("swiftbootAuthMessageSource")
    private MessageSource swiftbootAuthMessageSource;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (log.isDebugEnabled()) log.debug("do auth check for: %s".formatted(request.getRequestURI()));
        if (roles == null || roles.isEmpty()) {
            // ignore if no roles has been set.
            filterChain.doFilter(request, response);
            return;
        }
        // TODO: Workaround for loading locale from client header 'Accept-Language'
        LocaleContextHolder.setLocale(request.getLocale());
        // Get JWT token from HTTP request
        String token = super.getTokenFromRequest(request);
        try {
            String strRoles = jwtTokenProvider.getRoles(token);
            log.debug("User roles: %s".formatted(strRoles));
            String[] userRoles = StringUtils.split(strRoles, ",");
            if (userRoles != null && Arrays.stream(userRoles).anyMatch(role -> roles.contains(role))) {
                // pass
                filterChain.doFilter(request, response);
            }
            else {
                if (log.isWarnEnabled())
                    log.warn("User does not have the role that required: " + StringUtils.join(roles, ","));

                super.responseWithHttpStatus(response, HttpStatus.UNAUTHORIZED.value(), MessageHelper.getMessage(swiftbootAuthMessageSource, "swiftboot.auth.access.denied"));
            }
        } catch (JwtException | IOException e) {
            log.error(e.getMessage(), e);
            super.responseWithHttpStatus(response, HttpStatus.UNAUTHORIZED.value(), MessageHelper.getMessage(swiftbootAuthMessageSource, "swiftboot.auth.access.denied"));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            super.responseWithHttpStatus(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Server error");
        }
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getRoles() {
        return roles;
    }
}
