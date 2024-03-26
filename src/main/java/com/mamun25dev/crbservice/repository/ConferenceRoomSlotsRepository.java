package com.mamun25dev.crbservice.repository;

import com.mamun25dev.crbservice.domain.ConferenceRoomSlots;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ConferenceRoomSlotsRepository extends JpaRepository<ConferenceRoomSlots, Long> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("FROM ConferenceRoomSlots sl WHERE sl.id IN :idList")
    List<ConferenceRoomSlots> findAllSlotsByIdList(List<Long> idList);

}
