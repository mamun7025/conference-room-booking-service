package com.mamun25dev.crbservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import java.time.Instant;
import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "CONFERENCE_ROOM_SLOTS")
public class ConferenceRoomSlots {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conference_room_id")
    private ConferenceRoom conferenceRoom;

    private LocalDate slotDate;
    private String slotTimeWindow;
    private int status;

    // audit fields
    @CreatedDate
    @Column(updatable = false)
    private Instant createdDate;
}
