package org.swiftboot.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.swiftboot.auth.config.AuthConfigBean;
import org.swiftboot.auth.service.SessionService;
import org.swiftboot.web.exception.ErrMessageException;
import org.swiftboot.web.exception.ErrorCodeSupport;
import org.swiftboot.web.util.HttpServletCookieUtils;

import jakarta.annotation.Resource;

import java.io.IOException;
import java.util.Enumeration;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * 拦截客户端的请求，用 Header 或者 Cookie 中的令牌来验证用户请求的合法性。
 * 如果令牌不存在，会话不存在、过期或者无效，则抛出异常终止请求，返回相应的错误代码给客户端。
 * 使用 Spring 的 {@link org.springframework.boot.web.servlet.FilterRegistrationBean} 来定义需要进行过滤的 URL 模式
 *
 * @author swiftech
 */
@Order(Ordered.LOWEST_PRECEDENCE)
public class SessionAuthFilter extends BaseAuthFilter {

    private static final Logger log = LoggerFactory.getLogger(SessionAuthFilter.class);

    @Resource
    private SessionService sessionService;

    @Resource
    private AuthConfigBean configBean;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException {

        if (log.isDebugEnabled()) {
            log.debug(String.valueOf(request.getRequestURL()));
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String key = headerNames.nextElement();
                if (log.isDebugEnabled()) log.debug(String.format("  %s = %s", key, request.getHeader(key)));
            }
        }

        String tokenKey = configBean.getTokenKey();
        String token = HttpServletCookieUtils.getValueFromHeaderOrCookie(request, tokenKey);

        if (isBlank(token)) {
            if (log.isWarnEnabled()) log.warn(String.format("No token '%s' in Headers or Cookies", tokenKey));
            super.responseWithError(response, ErrorCodeSupport.CODE_NO_SIGNIN);
        }
        else {
            try {
                sessionService.verifySession(token);
                log.debug("User verified as valid");
                filterChain.doFilter(new TokenRequestWrapper(request, tokenKey), response);
            } catch (ErrMessageException e) {
                e.printStackTrace();
                super.responseWithError(response, e.getErrorCode());
            } catch (Exception e) {
                e.printStackTrace();
                super.responseWithError(response, ErrorCodeSupport.CODE_SYS_ERR);
            }
        }
    }

    /**
     * This wrapper transfer the token from cookie to header // TBD
     */
    static class TokenRequestWrapper extends HttpServletRequestWrapper {
        private final String tokenKey;

        public TokenRequestWrapper(HttpServletRequest request, String tokenKey) {
            super(request);
            this.tokenKey = tokenKey;
        }

        @Override
        public String getHeader(String name) {
            if (tokenKey.equals(name)) {
                HttpServletRequest req = (HttpServletRequest) getRequest();
                return HttpServletCookieUtils.getValueFromHeaderOrCookie(req, name);
            }
            return super.getHeader(name);
        }
    }


}
