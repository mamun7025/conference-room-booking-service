package com.mamun25dev.crbservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest{
    private Long roomId;                    // optional
    @NotBlank
    private String meetingStartTime;
    @NotBlank
    private String meetingEndTime;
    @NotNull
    @Min(value = 2, message = "minimum 2 people required")
    private Integer numberOfParticipants;
}
