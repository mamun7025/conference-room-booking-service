package com.mamun25dev.crbservice.service.adapter;

import com.mamun25dev.crbservice.domain.ConferenceRoom;
import com.mamun25dev.crbservice.dto.command.QueryOptimalRoomCommand;
import com.mamun25dev.crbservice.exception.BusinessException;
import com.mamun25dev.crbservice.repository.ConferenceRoomRepository;
import com.mamun25dev.crbservice.service.QueryOptimalRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.mamun25dev.crbservice.exception.ConferenceRoomErrorCode.OVERLAP_WITH_MAINTENANCE_FAILURE;


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

        // validation: maintenance time overlap
        slotList.stream()
                .filter(x -> x.getStatus() == -1)
                .findAny()
                .ifPresent(x -> {
                    throw new BusinessException(OVERLAP_WITH_MAINTENANCE_FAILURE);
                });

        return slotList.stream()
                .allMatch(x -> x.getStatus() == 0);
    }
}
