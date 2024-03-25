package com.mamun25dev.crbservice.repository;

import com.mamun25dev.crbservice.domain.ConferenceRoom;
import com.mamun25dev.crbservice.domain.MaintenanceSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MaintenanceSettingsRepository extends JpaRepository<MaintenanceSettings, Long> {

    List<MaintenanceSettings> findAllByConferenceRoom(ConferenceRoom conferenceRoom);
}
