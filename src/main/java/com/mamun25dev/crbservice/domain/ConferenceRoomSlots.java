package com.mamun25dev.crbservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.*;

@Setter
@Getter
@Entity
@Table(name = "CONFERENCE_ROOM_SLOTS")
@EntityListeners(AuditingEntityListener.class)
public class ConferenceRoomSlots {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conference_room_id")
    private ConferenceRoom conferenceRoom;

    private LocalDate slotDate;             // advance booking manage - not in current scope
    private String slotTimeWindow;
    @Transient
    private LocalTime slotStartTime;
    @Transient
    private LocalTime slotEndTime;
    private int status;

    // audit fields
    private String createdBy;
    @CreatedDate
    @Column(updatable = false)
    private Instant createdDate;

    public LocalTime getSlotStartTime(){
        return LocalTime.parse(slotTimeWindow.split("-")[0]);
    }
    public LocalTime getSlotEndTime(){
        return LocalTime.parse(slotTimeWindow.split("-")[1]);
    }
}
