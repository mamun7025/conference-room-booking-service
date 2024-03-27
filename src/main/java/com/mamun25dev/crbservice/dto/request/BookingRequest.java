package com.mamun25dev.crbservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest{
    private Long roomId;                    // optional
    @NotBlank(message = "meeting title required")
    private String meetingTitle;
    @NotBlank(message = "meeting start time required")
    private String meetingStartTime;
    @NotBlank(message = "meeting end time required")
    private String meetingEndTime;
    @NotNull
    @Min(value = 2, message = "minimum 2 people required")
    private Integer numberOfParticipants;
}
