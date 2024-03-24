package com.mamun25dev.crbservice.dto.command;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record QueryCommand(
        @NotBlank
        String startTime,
        @NotBlank
        String endTime
) {
}
