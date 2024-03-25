package com.mamun25dev.crbservice.dto;

import lombok.Builder;
import lombok.Data;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BookingDetails {

    private Long id;
    private Long roomId;
    private List<String> slotIds;
    private String meetingTitle;
    private String contactNumber;
    private String contactEmail;
    private int noOfParticipants;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String createdBy;
    private Instant createdDate;
}
