package com.chungnv.microservice.constant;

public enum ServiceEnum {
    SERVICE_ONE ("service-one"),
    SERVICE_TWO ("service-two"),
    AUTH ("auth");

    private String id;

    ServiceEnum(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

}
