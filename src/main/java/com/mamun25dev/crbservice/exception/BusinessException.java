package com.mamun25dev.crbservice.exception;


import lombok.Getter;

/**
 * business exception which can occur due to violation of business logic
 */
@Getter
public class BusinessException extends RuntimeException {

    private String errorCode;
    private String errorMessage;


    public BusinessException(Exception ex){
        super(ex);
    }

    public BusinessException(Exception ex, String errorCode, String errorMessage){
        super(ex);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public BusinessException(ConferenceRoomErrorCode errorDef){
        super(new Exception(errorDef.getErrorMessage()));
        this.errorCode = errorDef.getErrorCode();
        this.errorMessage = errorDef.getErrorMessage();
    }
}
