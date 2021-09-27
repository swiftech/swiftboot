package org.swiftboot.web.filter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;
import org.swiftboot.web.config.SwiftBootWebConfigBean;
import org.swiftboot.web.constant.HttpConstants;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 跨域过滤器，实现跨域访问的设置
 *
 * @deprecated
 * @author swiftech
 */
public class CorsFilter extends OncePerRequestFilter {

    private Logger log = LoggerFactory.getLogger(CorsFilter.class);

//    @Value("${swiftboot.web.filter.corsAllowOrigin:*}")
    private String corsAllowOrigin = "*";

//    @Value("${swiftboot.web.filter.allowHeaders}")
    private List<String> allowHeaders = new LinkedList<>();

    // 在这个 filter 中无法引用其他的 bean，暂时不使用
    @Resource
    SwiftBootWebConfigBean swiftBootConfigBean;

    private List<String> allAllowedHeaders = new ArrayList<String>() {
        {
            add(HttpConstants.DEFAULT_SESSION_ID_NAME);
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
//        allAllowedHeaders.addAll(allowHeaders);
//        log.info("Access-Control-Allow-Headers: " + StringUtils.join(allAllowedHeaders, ","));
//        log.info("Access-Control-Allow-Origin: " + corsAllowOrigin);
//        log.info("Access-Control-Allow-Origin: " + swiftBootConfigBean.getFilter().getCorsAllowOrigin());
//        log.info("Access-Control-Allow-Headers: " + StringUtils.join(swiftBootConfigBean.getFilter().getAllowHeaders(), ","));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        log.info(swiftBootConfigBean.getFilter().getCorsAllowOrigin());
        response.setHeader("Access-Control-Allow-Origin", swiftBootConfigBean.getFilter().getCorsAllowOrigin());
//        response.setHeader("Access-Control-Allow-Origin", swiftBootConfigBean.getFilter().getCorsAllowOrigin());
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", StringUtils.join(swiftBootConfigBean.getFilter().getAllowHeaders(), ","));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        filterChain.doFilter(request, response);
    }
}
