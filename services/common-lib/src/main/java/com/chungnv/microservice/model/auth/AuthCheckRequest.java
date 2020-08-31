package com.chungnv.microservice.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthCheckRequest {
    private String path;
    private String accessToken;
}
