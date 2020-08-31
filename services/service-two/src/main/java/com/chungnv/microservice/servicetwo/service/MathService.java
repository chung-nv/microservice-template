package com.chungnv.microservice.servicetwo.service;

import com.chungnv.microservice.model.math.AdditionRequest;
import com.chungnv.microservice.model.math.AdditionResponse;
import com.chungnv.microservice.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MathService {
    static final Logger logger = LoggerFactory.getLogger(MathService.class);

    public AdditionResponse add(AdditionRequest request) {
        logger.info("User: {}", CommonUtil.getCurrentAccessTokenPayload().getUsername());
        logger.info("Start adding: {}", request);
        AdditionResponse response = new AdditionResponse();
        response.setTotal(request.getA() + request.getB());
        logger.info("Adding result: {}", response);
        return response;
    }
}
