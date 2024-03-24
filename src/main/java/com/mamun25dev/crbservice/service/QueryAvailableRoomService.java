package com.mamun25dev.crbservice.service;

import com.mamun25dev.crbservice.dto.QueryCommand;
import com.mamun25dev.crbservice.dto.AvailableRoom;
import java.util.List;

public interface QueryAvailableRoomService {
    List<AvailableRoom> query(QueryCommand queryCommand);
}
