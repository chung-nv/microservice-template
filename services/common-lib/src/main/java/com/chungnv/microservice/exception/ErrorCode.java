package com.chungnv.microservice.exception;

import com.chungnv.microservice.model.ErrorResponse;

public enum ErrorCode {
    INVALID_CREDENTIALS(400, "00001", "Invalid username or password"),
    INSTANCE_NOT_FOUND(500, "00002", "Service instance not found: %s"),
    INTERNAL_SERVER_ERROR(500, "00003", "Internal server error"),
    INVALID_ACCESS_TOKEN(401, "00004", "Unauthorized");

    private Integer httpStatus;
    private String code;
    private String message;

    ErrorCode(Integer httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    public ApiException build(String... args) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setHttpStatus(this.httpStatus);
        errorResponse.setCode(this.code);
        errorResponse.setMessage(String.format(this.message, args));
        return new ApiException(errorResponse, true);
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
