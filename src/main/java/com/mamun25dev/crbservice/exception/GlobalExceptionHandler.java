package com.mamun25dev.crbservice.exception;

import com.mamun25dev.crbservice.dto.response.ResponseModel;
import com.mamun25dev.crbservice.dto.response.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(Exception ex, WebRequest request) {

        log.info("request process by global exception handler");
        BusinessException bzException = (BusinessException) ex;

        final var response  = ResponseModel.builder()
                .status(ResponseStatus.ERROR)
                .errorCode(bzException.getErrorCode())
                .message(bzException.getErrorMessage())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
