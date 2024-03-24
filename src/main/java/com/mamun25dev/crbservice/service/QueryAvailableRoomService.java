package com.mamun25dev.crbservice.service;

import com.mamun25dev.crbservice.dto.command.QueryCommand;
import com.mamun25dev.crbservice.dto.AvailableRoom;
import java.util.List;

public interface QueryAvailableRoomService {
    List<AvailableRoom> query(QueryCommand queryCommand);
}
