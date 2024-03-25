package com.mamun25dev.crbservice.dto.response;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ResponseStatus {
    SUCCESS("success"),
    FAIL("fail"),
    ERROR("error");

    private String valueSmall;

    ResponseStatus(String valueSmall){
        this.valueSmall = valueSmall;
    }
    public String getValueSmall(){
        return valueSmall;
    }

    @JsonValue
    public String toValue(){
        return ResponseStatus.valueOf(this.name()).getValueSmall();
    }
}
