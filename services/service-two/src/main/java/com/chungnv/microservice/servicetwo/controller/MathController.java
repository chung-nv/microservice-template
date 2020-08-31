package com.chungnv.microservice.servicetwo.controller;

import com.chungnv.microservice.model.math.AdditionRequest;
import com.chungnv.microservice.model.math.AdditionResponse;
import com.chungnv.microservice.servicetwo.service.MathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/math")
public class MathController {
    @Autowired
    MathService mathService;

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public AdditionResponse add(@RequestBody AdditionRequest requestBody) {
        return mathService.add(requestBody);
    }
}
