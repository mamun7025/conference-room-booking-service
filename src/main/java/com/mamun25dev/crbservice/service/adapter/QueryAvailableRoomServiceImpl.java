package com.mamun25dev.crbservice.service.adapter;

import com.mamun25dev.crbservice.domain.ConferenceRoom;
import com.mamun25dev.crbservice.dto.AvailableRoom;
import com.mamun25dev.crbservice.dto.SlotInfo;
import com.mamun25dev.crbservice.dto.command.QueryCommand;
import com.mamun25dev.crbservice.exception.BusinessException;
import com.mamun25dev.crbservice.repository.ConferenceRoomRepository;
import com.mamun25dev.crbservice.service.QueryAvailableRoomService;
import com.mamun25dev.crbservice.service.QuerySlotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import static com.mamun25dev.crbservice.exception.ConferenceRoomErrorCode.*;
import static com.mamun25dev.crbservice.exception.ConferenceRoomErrorCode.START_TIME_END_TIME_FAILURE;

@Slf4j
@Service
@RequiredArgsConstructor
public class QueryAvailableRoomServiceImpl implements QueryAvailableRoomService {

    private final Clock clock;
    private final ConferenceRoomRepository conferenceRoomRepository;
    private final QuerySlotService querySlotService;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");


    @Override
    public List<AvailableRoom> query(QueryCommand queryCommand) {
        validateBasic(queryCommand);

        LocalTime lkpStartTime = LocalTime.parse(queryCommand.startTime());
        LocalTime lkpEndTime = LocalTime.parse(queryCommand.endTime());
        // fetch all rooms
        // fetch that room available slots
        final var allRooms = conferenceRoomRepository.findAll();

        final var availableRoomSlots = allRooms.stream()
                .map(r -> mapToAvailableRoomCtx(r, lkpStartTime, lkpEndTime))
                .filter(Objects::nonNull)
                .toList();

        // no room business validation
        availableRoomSlots.stream()
                .findAny()
                .orElseThrow(() -> new BusinessException(CONFERENCE_ROOM_NOT_AVAILABLE_IN_RANGE));

        return availableRoomSlots;
    }

    private AvailableRoom mapToAvailableRoomCtx(ConferenceRoom conferenceRoom,
                                                LocalTime lkpStartTime, LocalTime lkpEndTime) {

        // sorted slots for this room
        final var sortedSlotList = querySlotService.query(conferenceRoom, lkpStartTime, lkpEndTime);

        // overlapped check
        sortedSlotList.stream()
                .filter(slot -> slot.getStatus() == -1)
                .findAny()
                .ifPresent(x -> {
                    throw new BusinessException(OVERLAP_WITH_MAINTENANCE_FAILURE);
                });

        final boolean allSlotAvailable = sortedSlotList.stream()
                .allMatch(slot -> slot.getStatus() == 0);

        if(allSlotAvailable){
            var availableSlots = sortedSlotList.stream()
                    .map(slot -> SlotInfo.builder()
                            .id(slot.getId())
                            .slotWindow(slot.getSlotTimeWindow())
                            .status(slot.getStatus())
                            .build())
                    .collect(Collectors.toList());

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

    private void validateBasic(QueryCommand queryCommand) {
        final var startTime = LocalTime.parse(queryCommand.startTime(), timeFormatter);
        final var endTime = LocalTime.parse(queryCommand.endTime(), timeFormatter);
        // past time
        final var todayTime = LocalTime.now(clock);
        if(todayTime.isAfter(startTime) || todayTime.isAfter(endTime))
            throw new BusinessException(BOOKING_TIME_VALIDATION_FAILURE);

        if(startTime.isAfter(endTime))
            throw new BusinessException(START_TIME_END_TIME_FAILURE);
    }

}
