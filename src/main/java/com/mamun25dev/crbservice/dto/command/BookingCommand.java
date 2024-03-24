package com.mamun25dev.crbservice.dto.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record BookingCommand(
        Long roomId,                        // optional
        @NotBlank
        String meetingStartTime,
        @NotBlank
        String meetingEndTime,
        @NotNull
        @Min(value = 2, message = "minimum 2 people required")
        Integer numberOfParticipants,

        String requestorUserId,             // login userId
        String requestorMobileNumber,       // login user mobile
        String requestorEmail               // login user email
) {
}
