package com.mamun25dev.crbservice.service;

import com.mamun25dev.crbservice.domain.ConferenceRoom;
import com.mamun25dev.crbservice.domain.ConferenceRoomSlots;
import com.mamun25dev.crbservice.repository.ConferenceRoomRepository;
import com.mamun25dev.crbservice.repository.ConferenceRoomSlotsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlotCreatorService {

    private final ConferenceRoomRepository conferenceRoomRepository;
    private final ConferenceRoomSlotsRepository conferenceRoomSlotsRepository;

    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

    public void createAllConferenceRoomSlots(){

        final var allRooms = conferenceRoomRepository.findAll();
        allRooms.forEach(x -> {
            createSlotForThisRoom(x);
        });
    }

    private void createSlotForThisRoom(ConferenceRoom room) {
        long totalOperationHour = computeOperationHour(room);                           // 9 hours - in general (9:00 AM to 18:00 PM)
        int slotUnit = computeSlotUnit(room.getSlotInterval());                         // 4 times
        int totalSlot = (int)totalOperationHour * slotUnit;                             // 36 for 15min interval
        log.info("calculated total slot: {}", totalSlot);

        List<ConferenceRoomSlots> slotInstList = new ArrayList<>();
        for (int i = 1; i <= totalSlot; i++){
            var slotTimeWindow = createSlotTimeWindow(room, i);
            var slotInst = new ConferenceRoomSlots();
            slotInst.setConferenceRoom(room);
            slotInst.setSlotDate(null);                                                 // advance booking not managing now
            slotInst.setSlotTimeWindow(slotTimeWindow);
            slotInst.setStatus(0);
            slotInst.setCreatedBy("system");
            slotInstList.add(slotInst);
        }
        // save all
        conferenceRoomSlotsRepository.saveAll(slotInstList);
    }

    private int computeSlotUnit(int slotInterval) {
        return 60 / slotInterval;           // 60min/15min
    }

    private Long computeOperationHour(ConferenceRoom room) {
        String operationHourWindow =  room.getWorkingHourWindow();
        LocalTime roomOperationHourStart = LocalTime.parse(operationHourWindow.split("-")[0].trim());
        LocalTime roomOperationHourEnd = LocalTime.parse(operationHourWindow.split("-")[1].trim());
        return ChronoUnit.HOURS.between(roomOperationHourStart, roomOperationHourEnd);
    }


    private String createSlotTimeWindow(ConferenceRoom room, int count) {
        String slotBeginHour = room.getWorkingHourWindow().split("-")[0].trim();
        int slotInterval = room.getSlotInterval();
        LocalTime slotStartTime = LocalTime.parse(slotBeginHour).plusMinutes((long) slotInterval * (count-1));
        LocalTime slotEndTime = slotStartTime.plusMinutes(slotInterval);
        return String.format("%s-%s", slotStartTime.format(timeFormat), slotEndTime.format(timeFormat));
    }
}
