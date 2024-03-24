package com.mamun25dev.crbservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SlotInfo {
    private Long id;
    private String slotWindow;
    private int status;
}
