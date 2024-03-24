package com.mamun25dev.crbservice.utils;

import com.mamun25dev.crbservice.dto.common.LoginUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class HttpHelper {

    private final HttpServletRequest httpServletRequest;

    public LoginUser getLoginUserInfo(){
        log.info("retrieve login user info...");
        final String userId = httpServletRequest.getHeader("X-USER-ID");
        final String userMobile = httpServletRequest.getHeader("X-USER-MOBILE");
        final String userEmail = httpServletRequest.getHeader("X-USER-EMAIL");
        return LoginUser.builder()
                .userId(userId)
                .mobileNumber(userMobile)
                .emailId(userEmail)
                .build();
    }

}
