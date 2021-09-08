package org.swiftboot.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;
import org.swiftboot.auth.SwiftbootAuthConfigBean;
import org.swiftboot.auth.service.SessionService;
import org.swiftboot.web.exception.ErrMessageException;
import org.swiftboot.web.exception.ErrorCodeSupport;
import org.swiftboot.web.result.HttpResponse;
import org.swiftboot.web.util.HttpServletCookieUtils;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.swiftboot.web.constant.HttpConstants.DEFAULT_RESPONSE_DATA_TYPE;

/**
 * 拦截客户端的请求，用 Header 或者 Cookie 中的令牌来验证用户会话的有效性。
 * 如果令牌不存在，会话不存在、过期或者无效，则抛出异常终止请求，返回默认的错误代码给客户端。
 * 使用 Spring 的 {@link org.springframework.boot.web.servlet.FilterRegistrationBean} 来定义需要进行过滤的 URL 模式
 *
 * @author swiftech
 */
@Order(Ordered.LOWEST_PRECEDENCE)
public class AuthFilter extends OncePerRequestFilter {

    private final Logger log = LoggerFactory.getLogger(AuthFilter.class);

    @Resource
    private SessionService sessionService;

    @Resource
    private SwiftbootAuthConfigBean configBean;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException {

        if (log.isDebugEnabled()) {
            log.debug(String.valueOf(request.getRequestURL()));
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String key = headerNames.nextElement();
                if (log.isDebugEnabled()) {
                    log.debug(String.format("  %s = %s", key, request.getHeader(key)));
                }
            }
        }

        String tokenKey = configBean.getSession().getTokenKey();
        String token = HttpServletCookieUtils.getValueFromHeaderOrCookie(request, tokenKey);

        if (isBlank(token)) {
            if (log.isWarnEnabled()) {
                log.warn(String.format("No token '%s' in Headers or Cookies", tokenKey));
            }
            this.responseWithError(response, ErrorCodeSupport.CODE_NO_SIGNIN);
        }
        else {
            try {
                sessionService.verifySession(token);
                log.debug("User verified as valid");
                filterChain.doFilter(new TokenRequestWrapper(request, tokenKey), response);
            } catch (ErrMessageException e) {
                e.printStackTrace();
                this.responseWithError(response, e.getErrorCode());
            } catch (Exception e) {
                e.printStackTrace();
                this.responseWithError(response, ErrorCodeSupport.CODE_SYS_ERR);
            }
        }
    }

    /**
     * This wrapper transfer the token from cookie to header, to make sure // TBD
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

    private void responseWithError(HttpServletResponse response, String errorCode) throws IOException {
        HttpResponse<?> resp = new HttpResponse<>(errorCode);
        response.setCharacterEncoding("utf-8");
        response.setContentType(DEFAULT_RESPONSE_DATA_TYPE);
        response.getWriter().write(new ObjectMapper().writeValueAsString(resp));
        response.flushBuffer();
        response.getWriter().close();
    }

//    private void responseWithHttpStatus(HttpServletResponse response) throws IOException {
//        response.setStatus(HttpStatus.FORBIDDEN.value());
//        response.flushBuffer();
//        response.getWriter().close();
//    }

}
