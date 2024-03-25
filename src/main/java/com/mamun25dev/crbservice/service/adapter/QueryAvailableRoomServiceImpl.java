package com.mamun25dev.crbservice.service.adapter;

import com.mamun25dev.crbservice.domain.ConferenceRoom;
import com.mamun25dev.crbservice.domain.ConferenceRoomSlots;
import com.mamun25dev.crbservice.dto.AvailableRoom;
import com.mamun25dev.crbservice.dto.SlotInfo;
import com.mamun25dev.crbservice.dto.command.QueryCommand;
import com.mamun25dev.crbservice.repository.ConferenceRoomRepository;
import com.mamun25dev.crbservice.service.QueryAvailableRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueryAvailableRoomServiceImpl implements QueryAvailableRoomService {

    private final ConferenceRoomRepository conferenceRoomRepository;
    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HHmm");

    @Override
    public List<AvailableRoom> query(QueryCommand queryCommand) {

        LocalTime lkpStartTime = LocalTime.parse(queryCommand.startTime());
        LocalTime lkpEndTime = LocalTime.parse(queryCommand.endTime());
        // fetch all rooms
        // fetch that room available slots
        final var allRooms = conferenceRoomRepository.findAll();

        final var availableRoomSlots = allRooms.stream()
                .map(r -> mapToAvailableRoomCtx(r, lkpStartTime, lkpEndTime))
                .filter(Objects::nonNull)
                .toList();

        return availableRoomSlots;
    }

    private AvailableRoom mapToAvailableRoomCtx(ConferenceRoom conferenceRoom,
                                                LocalTime lkpStartTime, LocalTime lkpEndTime) {

        final var allSlots = conferenceRoom.getRoomSlots();
        // sorted slots for this room
        final var sortedAllSlotsMap = allSlots
                .stream()
                .sorted(Comparator.comparing(x  -> x.getSlotStartTime()))
                .collect(Collectors.toMap(x -> x.getSlotStartTime().format(timeFormat), x -> x, (k1, k2) -> k1, LinkedHashMap::new));


        int startKeyLkp = Integer.parseInt(lkpStartTime.format(timeFormat));
        int endKeyLkp = Integer.parseInt(lkpEndTime.format(timeFormat));
        int keyInterval = conferenceRoom.getSlotInterval();

        List<SlotInfo> availableSlots = new ArrayList<>();
        boolean allSlotAvailable = true;
        for (int lkpKey = startKeyLkp; lkpKey < endKeyLkp; ){
            if(!isSlotAvailable(lkpKey, sortedAllSlotsMap)){
                allSlotAvailable = false;
                break;
            }
            addInSlotInfoList(availableSlots, lkpKey, sortedAllSlotsMap);
            lkpKey = getNextLookupKey(keyInterval, lkpKey);
        }

        if (allSlotAvailable){
            return AvailableRoom.builder()
                    .id(conferenceRoom.getId())
                    .name(conferenceRoom.getName())
                    .roomCode(conferenceRoom.getCode())
                    .capacity(conferenceRoom.getCapacity())
                    .slotList(availableSlots)
                    .build();
        }
        return null;
    }

    private int getNextLookupKey(int keyInterval, int lkpKey) {
        var lkpKeyTime = LocalTime.parse(String.valueOf(lkpKey), timeFormat);
        return Integer.parseInt(lkpKeyTime.plusMinutes(keyInterval).format(timeFormat));
    }

    private boolean isSlotAvailable(int lkpKey, LinkedHashMap<String, ConferenceRoomSlots> sortedSlotsMap){
        var lookupSlot = sortedSlotsMap.get(String.valueOf(lkpKey));
        return lookupSlot.getStatus() == 0;
    }

    private void addInSlotInfoList(List<SlotInfo> availableSlots, int lkpKey,
                                   LinkedHashMap<String, ConferenceRoomSlots> sortedAllSlotsMap) {
        var slotInst =  sortedAllSlotsMap.get(String.valueOf(lkpKey));
        availableSlots.add(
                SlotInfo.builder()
                        .id(slotInst.getId())
                        .slotWindow(slotInst.getSlotTimeWindow())
                        .status(slotInst.getStatus())
                        .build()
        );
    }

}
