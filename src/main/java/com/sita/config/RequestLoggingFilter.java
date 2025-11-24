package com.sita.config;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import com.sita.logging.AppLogger;
import com.sita.logging.Log;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestLoggingFilter implements Filter {

	private static final AppLogger log = Log.get(RequestLoggingFilter.class);

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
