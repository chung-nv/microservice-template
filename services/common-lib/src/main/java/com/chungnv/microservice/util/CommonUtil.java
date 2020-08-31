package com.chungnv.microservice.util;

import com.chungnv.microservice.model.AccessTokenPayload;
import com.chungnv.microservice.service.AccessTokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class CommonUtil {
    static final Logger logger = LoggerFactory.getLogger(CommonUtil.class);

    public static String toJson(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String json, Class<T> returnType) {
        try {
            return new ObjectMapper().readValue(json, returnType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static HttpServletRequest getCurrentHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            return request;
        }
        logger.info("Not called in the context of an HTTP request");
        return null;
    }

    public static AccessTokenPayload getCurrentAccessTokenPayload() {
        HttpServletRequest request = getCurrentHttpRequest();
        String internalAccessToken = request.getHeader("internalAccessToken");
        AccessTokenPayload payload = AccessTokenService.getInternalTokenPayload(internalAccessToken);
        return payload;
    }
}
