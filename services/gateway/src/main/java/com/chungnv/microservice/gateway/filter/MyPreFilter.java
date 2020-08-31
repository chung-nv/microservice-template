package com.chungnv.microservice.gateway.filter;

import com.chungnv.microservice.constant.ServiceEnum;
import com.chungnv.microservice.model.AccessTokenPayload;
import com.chungnv.microservice.model.auth.AuthCheckRequest;
import com.chungnv.microservice.model.auth.AuthCheckResponse;
import com.chungnv.microservice.service.AccessTokenService;
import com.chungnv.microservice.service.InternalRestTemplate;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;

public class MyPreFilter extends ZuulFilter {
    static final Logger logger = LoggerFactory.getLogger(MyPreFilter.class);

    private InternalRestTemplate internalRestTemplate;
    private AccessTokenService accessTokenService;

    public MyPreFilter(InternalRestTemplate internalRestTemplate, AccessTokenService accessTokenService) {
        this.internalRestTemplate = internalRestTemplate;
        this.accessTokenService = accessTokenService;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        setCorrelationId(ctx);

        logger.info("Start request: {} {}", request.getMethod(), request.getRequestURI());

        AccessTokenPayload accessTokenPayload = checkAuth(request);
        String internalAccessToken = accessTokenService.generateInternalToken(accessTokenPayload);
        ctx.addZuulRequestHeader("internalAccessToken", internalAccessToken);

        logRequest(request, accessTokenPayload);

        return null;
    }

    private void logRequest(HttpServletRequest request, AccessTokenPayload payload) {
        logger.info("Log request: user {} at {} call {} {}", payload.getUsername(), request.getRemoteAddr(),
                request.getMethod(), request.getRequestURI());
    }

    private void setCorrelationId(RequestContext ctx) {
        String correlationId = RandomStringUtils.randomAlphanumeric(20);
        MDC.put("correlationId", correlationId);
        ctx.addZuulRequestHeader("correlationId", correlationId);
    }

    private AccessTokenPayload checkAuth(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        String path = request.getRequestURI();

        AuthCheckRequest authCheckRequest = new AuthCheckRequest();
        authCheckRequest.setAccessToken(accessToken);
        authCheckRequest.setPath(path);

        AuthCheckResponse authCheckResponse = this.internalRestTemplate.post(ServiceEnum.AUTH, "/check", authCheckRequest, AuthCheckResponse.class);
        return authCheckResponse.getAccessTokenPayload();
    }
}
