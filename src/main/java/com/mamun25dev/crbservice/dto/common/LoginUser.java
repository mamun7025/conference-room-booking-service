package com.mamun25dev.crbservice.dto.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginUser {
    private String userId;
    private String mobileNumber;
    private String emailId;
}
