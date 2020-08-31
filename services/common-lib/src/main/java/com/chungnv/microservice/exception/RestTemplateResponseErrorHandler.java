package com.chungnv.microservice.exception;

import com.chungnv.microservice.model.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {
    static final Logger logger = LoggerFactory.getLogger(RestTemplateResponseErrorHandler.class);

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(ClientHttpResponse response)throws IOException {
        String body = "";
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getBody()))) {
            body = reader.lines().collect(Collectors.joining(""));
        }

        logger.error("Error response {}: {}", response.getStatusCode().value(), body);

        if (response.getHeaders().containsKey("isApiException")) {
            ErrorResponse errorResponse = new ObjectMapper().readValue(body, ErrorResponse.class);
            throw new ApiException(errorResponse, false);
        } else {
            throw ErrorCode.INTERNAL_SERVER_ERROR.build();
        }
    }
}
