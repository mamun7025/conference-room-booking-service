package com.mamun25dev.crbservice.service;

import com.mamun25dev.crbservice.domain.ConferenceRoom;
import com.mamun25dev.crbservice.dto.command.QueryOptimalRoomCommand;
import java.util.Optional;

public interface QueryOptimalRoomService {
    Optional<ConferenceRoom> query(QueryOptimalRoomCommand queryCommand);
}