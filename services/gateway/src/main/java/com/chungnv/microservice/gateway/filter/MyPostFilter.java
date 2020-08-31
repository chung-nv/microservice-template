package com.chungnv.microservice.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

public class MyPostFilter extends ZuulFilter {
    static final Logger logger = LoggerFactory.getLogger(MyPostFilter.class);

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 1000;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();

        logger.info("End request: {} {} with status {}", request.getMethod(), request.getRequestURI(), ctx.getResponseStatusCode());

        return null;
    }
}
