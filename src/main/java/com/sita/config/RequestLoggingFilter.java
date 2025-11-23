package com.sita.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestLoggingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();

        // Skip logs for static resources
        if (path.startsWith("/css")
                || path.startsWith("/js")
                || path.startsWith("/images")
                || path.equals("/favicon.ico")) {

            chain.doFilter(request, response);
            return;
        }

        // Generate requestId
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);

        long start = System.currentTimeMillis();

        log.info("Incoming request: method={}, path={}, requestId={}",
                req.getMethod(), path, requestId);

        try {
            chain.doFilter(request, response);

        } finally {
            long duration = System.currentTimeMillis() - start;

            log.info("Completed request: method={}, path={}, status={}, durationMs={}, requestId={}",
                    req.getMethod(), path, res.getStatus(), duration, requestId);

            MDC.clear();
        }
    }
}
