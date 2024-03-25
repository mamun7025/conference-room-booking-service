package com.mamun25dev.crbservice.repository;

import com.mamun25dev.crbservice.domain.RoomBookingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomBookingHistoryRepository extends JpaRepository<RoomBookingHistory, Long> {
}
