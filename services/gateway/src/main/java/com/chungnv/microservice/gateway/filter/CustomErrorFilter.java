package com.chungnv.microservice.gateway.filter;

import com.chungnv.microservice.exception.ApiException;
import com.chungnv.microservice.exception.ErrorCode;
import com.chungnv.microservice.util.CommonUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomErrorFilter extends ZuulFilter {
    static final Logger logger = LoggerFactory.getLogger(CustomErrorFilter.class);

    @Override
    public String filterType() {
        return "error";
    }

    @Override
    public int filterOrder() {
        return -1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return ctx.getThrowable() != null;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        Throwable e = ctx.getThrowable();
        ApiException apiException = ErrorCode.INTERNAL_SERVER_ERROR.build();
        if (e.getCause() instanceof ApiException) {
            apiException = (ApiException) e.getCause();
            if (apiException.isInternal()) {
                logger.error("API Exception", apiException);
            }
        } else {
            logger.error("Zuul error", e);
        }
        ctx.setResponseBody(CommonUtil.toJson(apiException.getErrorResponse()));
        ctx.getResponse().setContentType("application/json");
        ctx.getResponse().setHeader("isApiException", "");
        ctx.setResponseStatusCode(apiException.getErrorResponse().getHttpStatus());
        ctx.remove("throwable");
        return null;
    }
}
