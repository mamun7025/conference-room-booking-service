package com.mamun25dev.crbservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.Instant;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "CONFERENCE_ROOM")
@EntityListeners(AuditingEntityListener.class)
public class ConferenceRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String code;
    private int capacity;
    private int slotInterval;
    private String workingHourWindow;

    @OneToMany(mappedBy = "conferenceRoom", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ConferenceRoomSlots> roomSlots;

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
