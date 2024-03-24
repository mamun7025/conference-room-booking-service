package com.mamun25dev.crbservice.repository;

import com.mamun25dev.crbservice.domain.ConferenceRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConferenceRoomRepository extends JpaRepository<ConferenceRoom, Long> {
}
