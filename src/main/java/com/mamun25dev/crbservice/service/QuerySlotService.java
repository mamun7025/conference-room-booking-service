package com.mamun25dev.crbservice.service;

import com.mamun25dev.crbservice.domain.ConferenceRoom;
import com.mamun25dev.crbservice.domain.ConferenceRoomSlots;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuerySlotService {

    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HHmm");


    public List<ConferenceRoomSlots> query(ConferenceRoom conferenceRoom,
                                           LocalTime lkpStartTime, LocalTime lkpEndTime) {

        final var allSlots = conferenceRoom.getRoomSlots();
        // sorted slots for this room
        final var sortedSlotsMap = allSlots
                .stream()
                .sorted(Comparator.comparing(x  -> x.getSlotStartTime()))
                .collect(Collectors.toMap(x -> x.getSlotStartTime().format(timeFormat), x -> x, (k1, k2) -> k1, LinkedHashMap::new));


        int startKeyLkp = Integer.parseInt(lkpStartTime.format(timeFormat));
        int endKeyLkp = Integer.parseInt(lkpEndTime.format(timeFormat));
        int keyInterval = conferenceRoom.getSlotInterval();

        List<ConferenceRoomSlots> slotList = new ArrayList<>();
        for (int lkpKey = startKeyLkp; lkpKey < endKeyLkp; ){
            var lookupSlot = sortedSlotsMap.get(String.format("%04d", lkpKey));
            slotList.add(lookupSlot);
            lkpKey = getNextLookupKey(keyInterval, lkpKey);
        }
        return slotList;
    }

    private int getNextLookupKey(int keyInterval, int lkpKey) {
        var lkpKeyTime = LocalTime.parse(String.format("%04d", lkpKey), timeFormat);
        return Integer.parseInt(lkpKeyTime.plusMinutes(keyInterval).format(timeFormat));
    }

}
