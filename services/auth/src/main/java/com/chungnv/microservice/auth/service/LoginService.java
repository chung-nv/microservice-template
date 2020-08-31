package com.chungnv.microservice.auth.service;

import com.chungnv.microservice.exception.ErrorCode;
import com.chungnv.microservice.model.AccessTokenPayload;
import com.chungnv.microservice.model.auth.LoginRequest;
import com.chungnv.microservice.model.auth.LoginResponse;
import com.chungnv.microservice.service.AccessTokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    AccessTokenService accessTokenService;

    public LoginResponse login(LoginRequest requestBody) {
        logger.info("Login username: {}", requestBody.getUsername());

        String username = requestBody.getUsername();
        String password = requestBody.getPassword();

        validate(username, password);

        String accessToken = createAccessToken(username);

        LoginResponse response = new LoginResponse();
        response.setAccessToken(accessToken);
        return response;
    }

    private String createAccessToken(String username) {
        AccessTokenPayload payload = new AccessTokenPayload();
        payload.setUsername(username);
        payload.setExpiredTime(System.currentTimeMillis() + 900000); // 15 minutes

        String token = accessTokenService.generateExternalToken(payload);

        // TODO: save access token hash to database

        return token;
    }

    private void validate(String username, String password) {
        if (username == null || password == null) {
            throw ErrorCode.INVALID_CREDENTIALS.build();
        }

        if (!username.equals("admin") || !password.equals("admin")) { // TODO: just for demo, should check in database
            throw ErrorCode.INVALID_CREDENTIALS.build();
        }
    }
}
