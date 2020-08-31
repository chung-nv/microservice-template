package com.chungnv.microservice.auth.controller;

import com.chungnv.microservice.auth.service.LoginService;
import com.chungnv.microservice.model.auth.LoginRequest;
import com.chungnv.microservice.model.auth.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    LoginService loginService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public LoginResponse login(@RequestBody LoginRequest requestBody) {
        return loginService.login(requestBody);
    }
}
