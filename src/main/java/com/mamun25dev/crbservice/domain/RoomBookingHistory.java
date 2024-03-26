package com.mamun25dev.crbservice.domain;

import com.mamun25dev.crbservice.utils.CustomAttributeConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "ROOM_BOOKING_HISTORY")
@EntityListeners(AuditingEntityListener.class)
public class RoomBookingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "conference_room_id")
    private ConferenceRoom conferenceRoom;

    private String meetingTitle;
    private String contactNumber;
    private String contactEmail;
    private int noOfParticipants;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @Convert(converter = CustomAttributeConverter.class)
    private List<String> slotIds = new ArrayList<>(1);

    // audit fields
    private String createdBy;
    private String lastModifiedBy;
    @CreatedDate
    @Column(updatable = false)
    private Instant createdDate;
    @LastModifiedBy
    @Column
    private Instant lastModifiedDate;
}
