package org.swiftboot.common.auth.aop;

import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.swiftboot.common.auth.JwtUtils;
import org.swiftboot.common.auth.config.AuthConfigBean;
import org.swiftboot.common.auth.config.CookieConfigBean;
import org.swiftboot.common.auth.config.JwtConfigBean;

import java.util.Arrays;
import java.util.Optional;

/**
 * @since 3.2.1
 */
@Component
public class TokenHelper {

    @Resource
    private AuthConfigBean authConfigBean;

    @Resource
    private JwtConfigBean jwtConfigBean;

    /**
     *
     * @param clientSource
     * @param name
     * @param value
     * @return
     */
    public Cookie createTokenCookie(String clientSource, String name, String value) {
        return createTokenCookie(clientSource, name, value, null);
    }

    /**
     *
     * @param clientSource
     * @param name
     * @param value
     * @param maxAge       忽略配置，强制使用时间，用于清除cookie
     * @return
     */
    public Cookie createTokenCookie(String clientSource, String name, String value, Integer maxAge) {
        CookieConfigBean accessTokenCookieConfig = jwtConfigBean.determineAccessTokenCookie(clientSource);
        Cookie tokenCookie = new Cookie(name, value);
        tokenCookie.setDomain(accessTokenCookieConfig.getDomain());
        tokenCookie.setPath(accessTokenCookieConfig.getPath());
        tokenCookie.setHttpOnly(accessTokenCookieConfig.getHttpOnly());
        tokenCookie.setSecure(accessTokenCookieConfig.getSecure());
        tokenCookie.setMaxAge(maxAge != null ? maxAge : accessTokenCookieConfig.getMaxAge());
        return tokenCookie;
    }


    public String getTokenFromRequest(HttpServletRequest request) {
        String token = this.getTokenFromCookie(request);
        if (!StringUtils.hasText(token)) {
            token = this.getTokenFromHeader(request);
        }
        return token;
    }

    /**
     * Be used for JWT
     *
     * @param request
     * @return
     */
    public String getTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        return JwtUtils.extractBearerToken(bearerToken);
    }

    /**
     *
     * @param request
     * @return
     */
    public String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        Optional<Cookie> found = Arrays.stream(cookies).filter(c -> authConfigBean.getTokenKey().equalsIgnoreCase(c.getName())).findFirst();
        if (found.isPresent()) {
            return found.get().getValue();
        }
        return null;
    }
}
