package com.chungnv.microservice.interceptor;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LogInterceptor extends HandlerInterceptorAdapter {
    static final Logger logger = LoggerFactory.getLogger(LogInterceptor.class);

    @Value("${spring.application.name}")
    private String serviceName;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        setCorrelationId(request);
        setInternalCorrelationId();
        setServiceName();
        logRequestStart(request);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        logRequestEnd(request);
    }

    private void logRequestEnd(HttpServletRequest request) {
        logger.info("End request: {} {}", request.getMethod(), request.getRequestURI());
    }

    private void logRequestStart(HttpServletRequest request) {
        logger.info("Start request: {} {}", request.getMethod(), request.getRequestURI());
    }

    private void setServiceName() {
        MDC.put("serviceName", serviceName);
    }

    private void setInternalCorrelationId() {
        String internalCorrelationId = RandomStringUtils.randomAlphanumeric(20);
        MDC.put("internalCorrelationId", internalCorrelationId);
    }

    private void setCorrelationId(HttpServletRequest request) {
        String correlationId = request.getHeader("correlationId");
        MDC.put("correlationId", correlationId);
    }
}
