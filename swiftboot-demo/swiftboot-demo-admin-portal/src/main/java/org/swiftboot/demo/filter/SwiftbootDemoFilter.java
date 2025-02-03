package org.swiftboot.demo.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SwiftbootDemoFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(SwiftbootDemoFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("执行 SwiftbootDemoFilter");
        filterChain.doFilter(request, response);
    }
}
