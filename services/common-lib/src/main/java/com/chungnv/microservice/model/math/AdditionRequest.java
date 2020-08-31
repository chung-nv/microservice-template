package com.chungnv.microservice.model.math;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdditionRequest {
    private double a;
    private double b;
}
