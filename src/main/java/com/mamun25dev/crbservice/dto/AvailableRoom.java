package com.mamun25dev.crbservice.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class AvailableRoom {
    private Long id;
    private String name;
    private String roomCode;
    private int capacity;
    private List<SlotInfo> slotList;
}
