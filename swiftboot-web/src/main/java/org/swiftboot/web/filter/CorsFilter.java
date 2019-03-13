package org.swiftboot.web.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;
import org.swiftboot.web.constant.HttpConstants;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 跨域过滤器，实现跨域访问的设置
 *
 * @author swiftech
 */
public class CorsFilter extends OncePerRequestFilter {

    private Logger log = LoggerFactory.getLogger(CorsFilter.class);

    @Value("${http.cors.allow.origin:*}")
    private String corsAllowOrigin = "*";

    // TODO configurable
    private List<String> allowedHeaders = new ArrayList<String>() {
        {
            add(HttpConstants.SESSION_ID_NAME);
            add("Content-Type");
            add("Access-Control-Allow-Headers");
            add("Authorization");
            add("X-Requested-With");
            add("Access-Control-Request-Headers");
            add("Access-Control-Request-Method");
        }
    };

    public CorsFilter() {
        log.info("初始化跨域过滤器");
        log.info("Access-Control-Allow-Origin: " + corsAllowOrigin);
        log.info("Access-Control-Allow-Headers: " + StringUtils.join(allowedHeaders, ","));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", corsAllowOrigin);
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", StringUtils.join(allowedHeaders, ","));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        filterChain.doFilter(request, response);
    }
}
