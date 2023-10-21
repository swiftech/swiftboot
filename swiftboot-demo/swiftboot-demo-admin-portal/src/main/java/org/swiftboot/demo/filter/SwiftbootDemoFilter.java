package org.swiftboot.demo.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SwiftbootDemoFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(SwiftbootDemoFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("执行 SwiftbootDemoFilter");
        filterChain.doFilter(request, response);
    }
}
