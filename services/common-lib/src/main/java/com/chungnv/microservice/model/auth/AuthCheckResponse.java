package com.chungnv.microservice.model.auth;

import com.chungnv.microservice.model.AccessTokenPayload;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthCheckResponse {
    private AccessTokenPayload accessTokenPayload;
}
