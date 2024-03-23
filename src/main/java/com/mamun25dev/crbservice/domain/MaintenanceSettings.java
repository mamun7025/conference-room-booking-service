package com.mamun25dev.crbservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name = "MAINTENANCE_SETTINGS")
public class MaintenanceSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "conference_room_id")
    private ConferenceRoom conferenceRoom;

    private String maintenanceSlot;

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
