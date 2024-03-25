package com.mamun25dev.crbservice.dto.command;

import lombok.Builder;
import java.time.LocalTime;

@Builder
public record QueryOptimalRoomCommand(
        int noOfParticipant,
        LocalTime meetingStartTime,
        LocalTime meetingEndTime
) {
}
