package com.mamun25dev.crbservice.exception;

import lombok.Getter;

@Getter
public enum ConferenceRoomErrorCode {

    ROOM_NOT_AVAILABLE("CR-0001", "No conference room available")
    ;


    private String errorCode;
    private String errorMessage;


    ConferenceRoomErrorCode(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
