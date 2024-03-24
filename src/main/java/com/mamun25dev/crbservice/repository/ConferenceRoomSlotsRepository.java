package com.mamun25dev.crbservice.repository;

import com.mamun25dev.crbservice.domain.ConferenceRoomSlots;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ConferenceRoomSlotsRepository extends JpaRepository<ConferenceRoomSlots, Long> {

    @Query("FROM ConferenceRoomSlots WHERE status = 0")
    List<ConferenceRoomSlots> findAllAvailableRoomSlot();

}
