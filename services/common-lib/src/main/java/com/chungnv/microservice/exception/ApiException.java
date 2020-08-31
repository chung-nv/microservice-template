package com.chungnv.microservice.exception;

import com.chungnv.microservice.model.ErrorResponse;

public class ApiException extends RuntimeException {
    private ErrorResponse errorResponse;
    private boolean isInternal; // Exception fired within current service or not

    public ApiException(ErrorResponse errorResponse, boolean isInternal) {
        super(errorResponse.toString());
        this.errorResponse = errorResponse;
        this.isInternal = isInternal;
    }

    public ErrorResponse getErrorResponse() {
        return errorResponse;
    }

    public void setErrorResponse(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public boolean isInternal() {
        return isInternal;
    }

    public void setInternal(boolean internal) {
        isInternal = internal;
    }
}
