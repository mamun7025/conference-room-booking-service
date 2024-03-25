package com.mamun25dev.crbservice.service;

import com.mamun25dev.crbservice.domain.ConferenceRoom;
import com.mamun25dev.crbservice.domain.ConferenceRoomSlots;
import java.time.LocalTime;
import java.util.List;

public interface QuerySlotService {
    List<ConferenceRoomSlots> query(ConferenceRoom conferenceRoom,
                                    LocalTime lkpStartTime, LocalTime lkpEndTime);
}
