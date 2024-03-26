package com.mamun25dev.crbservice.service.adapter;

import com.mamun25dev.crbservice.domain.ConferenceRoom;
import com.mamun25dev.crbservice.dto.command.QueryOptimalRoomCommand;
import com.mamun25dev.crbservice.repository.ConferenceRoomRepository;
import com.mamun25dev.crbservice.service.QueryOptimalRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class QueryOptimalRoomServiceImpl implements QueryOptimalRoomService {


    private final QuerySlotServiceImpl querySlotServiceImpl;
    private final ConferenceRoomRepository conferenceRoomRepository;

    @Override
    public Optional<ConferenceRoom> query(QueryOptimalRoomCommand queryCommand) {
        // move next room, if slot not available
        final List<ConferenceRoom> roomList = conferenceRoomRepository.findAllByCapacityGreaterThanEqual(queryCommand.noOfParticipant());

        return roomList.stream()
                .filter(x -> checkSlotsAvailableWithinRange(x,
                        queryCommand.meetingStartTime(),
                        queryCommand.meetingEndTime()))
                .findFirst();
    }

    private boolean checkSlotsAvailableWithinRange(ConferenceRoom room, LocalTime startTime, LocalTime endTime) {
        final var slotList = querySlotServiceImpl.query(room, startTime, endTime);
        return slotList.stream()
                .allMatch(x -> x.getStatus() == 0);
    }
}
