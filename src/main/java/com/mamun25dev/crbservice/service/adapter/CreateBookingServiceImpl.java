package com.mamun25dev.crbservice.service.adapter;

import com.mamun25dev.crbservice.domain.ConferenceRoom;
import com.mamun25dev.crbservice.domain.RoomBookingHistory;
import com.mamun25dev.crbservice.dto.command.BookingCommand;
import com.mamun25dev.crbservice.dto.BookingDetails;
import com.mamun25dev.crbservice.repository.ConferenceRoomRepository;
import com.mamun25dev.crbservice.repository.ConferenceRoomSlotsRepository;
import com.mamun25dev.crbservice.repository.RoomBookingHistoryRepository;
import com.mamun25dev.crbservice.service.CreateBookingService;
import com.mamun25dev.crbservice.service.QuerySlotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateBookingServiceImpl implements CreateBookingService {

    private final ConferenceRoomRepository roomRepository;
    private final ConferenceRoomSlotsRepository slotsRepository;
    private final RoomBookingHistoryRepository historyRepository;
    private final QuerySlotService querySlotService;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    @Override
    public BookingDetails create(BookingCommand command) {
        // fetch conference room
        final var room= roomRepository.findById(command.roomId())
                .orElseThrow( () -> new RuntimeException("") );

        // query slots by roomId timeRange
        final var slotList = querySlotService.querySlots(room,
                LocalDateTime.parse(command.meetingStartTime(), dateTimeFormatter).toLocalTime(),
                LocalDateTime.parse(command.meetingEndTime(), dateTimeFormatter).toLocalTime());

        // book slot
        final var updateSlotList = slotList.stream()
                .map(x -> {
                    x.setStatus(1);
                    return x;
                })
                .collect(Collectors.toList());

        // save all
        slotsRepository.saveAll(updateSlotList);


        // insert into history
        var bookingHistory = new RoomBookingHistory();
        bookingHistory.setConferenceRoom(room);
        bookingHistory.setMeetingTitle(command.meetingTitle());
        bookingHistory.setNoOfParticipants(command.numberOfParticipants());
        bookingHistory.setStartTime(LocalDateTime.parse(command.meetingStartTime()));
        bookingHistory.setEndTime(LocalDateTime.parse(command.meetingEndTime()));
        bookingHistory.setContactNumber(command.requestorMobileNumber());
        bookingHistory.setContactEmail(command.requestorEmail());
        bookingHistory.setCreatedBy(command.requestorUserId());
        bookingHistory.setSlotIds(slotList.stream().map(x -> x.getId().toString()).collect(Collectors.toList()));
        historyRepository.save(bookingHistory);


        return BookingDetails.builder()
                .roomId(room.getId())
                .slotIds(slotList.stream().map(x -> x.getId().toString()).collect(Collectors.toList()))
                .noOfParticipants(bookingHistory.getNoOfParticipants())
                .meetingTitle(bookingHistory.getMeetingTitle())
                .contactNumber(bookingHistory.getContactNumber())
                .contactEmail(bookingHistory.getContactEmail())
                .startTime(bookingHistory.getStartTime())
                .endTime(bookingHistory.getEndTime())
                .build();
    }

    private void createBookingWithRoomId(){
    }

    private void createBookingWithTimeRange(){
    }

    private void queryAllSlotsByRoomIdTimeRange(ConferenceRoom room, String meetingStartTime, String meetingEndTime){
    }
}
