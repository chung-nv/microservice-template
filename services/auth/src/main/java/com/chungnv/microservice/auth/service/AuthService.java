package com.chungnv.microservice.auth.service;

import com.chungnv.microservice.exception.ErrorCode;
import com.chungnv.microservice.model.AccessTokenPayload;
import com.chungnv.microservice.model.auth.AuthCheckRequest;
import com.chungnv.microservice.model.auth.AuthCheckResponse;
import com.chungnv.microservice.service.AccessTokenService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.chungnv.microservice.auth.constant.AuthConstants.PUBLIC_URLS;

@Service
public class AuthService {
    static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    AccessTokenService accessTokenService;

    public AuthCheckResponse check(AuthCheckRequest request) {
        logger.info("Check auth: {}", request.getPath());

        AccessTokenPayload accessTokenPayload = new AccessTokenPayload();

        if (!isPublic(request.getPath())) {
            accessTokenPayload = validateAccessToken(request.getAccessToken());
        }

        AuthCheckResponse response = new AuthCheckResponse();
        response.setAccessTokenPayload(accessTokenPayload);
        return response;
    }

    private AccessTokenPayload validateAccessToken(String accessToken) {
        if (StringUtils.isBlank(accessToken)) {
            logger.error("Token is blank");
            throw ErrorCode.INVALID_ACCESS_TOKEN.build();
        }

        AccessTokenPayload payload = accessTokenService.getExternalTokenPayload(accessToken);

        if (payload.getExpiredTime() == null || payload.getExpiredTime() < System.currentTimeMillis()) {
            logger.error("Token is expired");
            throw ErrorCode.INVALID_ACCESS_TOKEN.build();
        }

        // TODO: check access token hash exist in database

        return payload;
    }

    private boolean isPublic(String path) {
        for (String uri : PUBLIC_URLS) {
            if (path.startsWith(uri)) {
                return true;
            }
        }
        return false;
    }
}
