package com.mamun25dev.crbservice.dto.common;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ResponseStatus {
    SUCCESS("success"),
    FAIL("fail"),
    ERROR("error");

    private String value;

    ResponseStatus(String value){
    }
    public String getValue(){
        return value;
    }

    @JsonValue
    public String toValue(){
        return this.name().toLowerCase();
    }
}
