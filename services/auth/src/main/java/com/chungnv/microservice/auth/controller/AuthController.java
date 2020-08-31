package com.chungnv.microservice.auth.controller;

import com.chungnv.microservice.auth.service.AuthService;
import com.chungnv.microservice.model.auth.AuthCheckRequest;
import com.chungnv.microservice.model.auth.AuthCheckResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/check")
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthCheckResponse login(@RequestBody AuthCheckRequest requestBody) {
        return authService.check(requestBody);
    }
}
