package com.chungnv.microservice.exception;

import com.chungnv.microservice.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException apiException) {
        if (apiException.isInternal()) {
            logger.error("API Exception", apiException);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("isApiException", "");
        headers.setContentType(MediaType.APPLICATION_JSON);
        return ResponseEntity.status(apiException.getErrorResponse().getHttpStatus())
                .headers(headers)
                .body(apiException.getErrorResponse());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ApiException apiException = ErrorCode.INTERNAL_SERVER_ERROR.build();
        apiException.initCause(ex);
        return handleApiException(apiException);
    }
}
