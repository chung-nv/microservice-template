package com.chungnv.microservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessTokenPayload {
    private String username;
    private Long expiredTime;
    private String padding;
}
