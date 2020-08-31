package com.chungnv.microservice.serviceone.controller;

import com.chungnv.microservice.constant.ServiceEnum;
import com.chungnv.microservice.model.math.AdditionRequest;
import com.chungnv.microservice.model.math.AdditionResponse;
import com.chungnv.microservice.service.InternalRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/math")
public class MathController {
    @Autowired
    InternalRestTemplate internalRestTemplate;

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public AdditionResponse add(@RequestBody AdditionRequest requestBody) {
        return internalRestTemplate.post(ServiceEnum.SERVICE_TWO, "/math/add", requestBody, AdditionResponse.class);
    }
}
