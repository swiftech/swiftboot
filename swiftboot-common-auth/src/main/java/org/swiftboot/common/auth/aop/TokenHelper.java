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

import java.util.Arrays;
import java.util.Optional;

/**
 * @since 3.2.1
 */
@Component
public class TokenHelper {

    @Resource
    private AuthConfigBean authConfigBean;

    /**
     *
     * @param tokenCookieConfig
     * @param name
     * @param value
     * @return
     */
    public Cookie createTokenCookie(CookieConfigBean tokenCookieConfig, String name, String value) {
        return this.createTokenCookie(tokenCookieConfig, name, value, null);
    }

    /**
     *
     * @param tokenCookieConfig
     * @param name
     * @param value
     * @param maxAge
     * @return
     */
    public Cookie createTokenCookie(CookieConfigBean tokenCookieConfig, String name, String value, Integer maxAge) {
        Cookie tokenCookie = new Cookie(name, value);
        tokenCookie.setDomain(tokenCookieConfig.getDomain());
        tokenCookie.setPath(tokenCookieConfig.getPath());
        tokenCookie.setHttpOnly(tokenCookieConfig.getHttpOnly());
        tokenCookie.setSecure(tokenCookieConfig.getSecure());
        tokenCookie.setMaxAge(maxAge != null ? maxAge : tokenCookieConfig.getMaxAge());
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
