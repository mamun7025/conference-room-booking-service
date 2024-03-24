package com.mamun25dev.crbservice.dto.common;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseModel<T> {

    private ResponseStatus status;
    private Integer statusCode;
    private T data;
    private String message;
    private String errorCode;

}
