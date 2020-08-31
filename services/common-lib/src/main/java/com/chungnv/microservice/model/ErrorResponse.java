package com.chungnv.microservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {
    private Integer httpStatus;
    private String code;
    private String message;
}
