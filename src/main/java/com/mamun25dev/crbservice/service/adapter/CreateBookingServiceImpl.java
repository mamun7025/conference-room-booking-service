package com.mamun25dev.crbservice.service.adapter;

import com.mamun25dev.crbservice.domain.ConferenceRoom;
import com.mamun25dev.crbservice.domain.ConferenceRoomSlots;
import com.mamun25dev.crbservice.domain.RoomBookingHistory;
import com.mamun25dev.crbservice.dto.command.BookingCommand;
import com.mamun25dev.crbservice.dto.BookingDetails;
import com.mamun25dev.crbservice.dto.command.QueryOptimalRoomCommand;
import com.mamun25dev.crbservice.repository.ConferenceRoomRepository;
import com.mamun25dev.crbservice.repository.ConferenceRoomSlotsRepository;
import com.mamun25dev.crbservice.repository.RoomBookingHistoryRepository;
import com.mamun25dev.crbservice.service.CreateBookingService;
import com.mamun25dev.crbservice.service.QueryOptimalRoomService;
import com.mamun25dev.crbservice.service.QuerySlotService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateBookingServiceImpl implements CreateBookingService {

    private final ConferenceRoomRepository roomRepository;
    private final ConferenceRoomSlotsRepository slotsRepository;
    private final RoomBookingHistoryRepository historyRepository;

    private final QuerySlotService querySlotService;
    private final QueryOptimalRoomService queryOptimalRoomService;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    @Override
    public BookingDetails create(BookingCommand command) {

        var savedBookingCtx = (command.roomId() != null)
                ? createBookingWithRoomId(command)
                : createBookingWithOptimalSearch(command);

        var bookedSlotIds = savedBookingCtx.getBookedSlots().stream()
                .map(x -> x.getId().toString())
                .collect(Collectors.toList());

        // insert into history
        var bookingHistory = new RoomBookingHistory();
        bookingHistory.setConferenceRoom(savedBookingCtx.bookedRoom);
        bookingHistory.setMeetingTitle(command.meetingTitle());
        bookingHistory.setNoOfParticipants(command.numberOfParticipants());
        bookingHistory.setStartTime(LocalDateTime.parse(command.meetingStartTime()));
        bookingHistory.setEndTime(LocalDateTime.parse(command.meetingEndTime()));
        bookingHistory.setContactNumber(command.requestorMobileNumber());
        bookingHistory.setContactEmail(command.requestorEmail());
        bookingHistory.setCreatedBy(command.requestorUserId());
        bookingHistory.setSlotIds(bookedSlotIds);
        historyRepository.save(bookingHistory);


        return BookingDetails.builder()
                .roomId(bookingHistory.getConferenceRoom().getId())
                .slotIds(bookedSlotIds)
                .noOfParticipants(bookingHistory.getNoOfParticipants())
                .meetingTitle(bookingHistory.getMeetingTitle())
                .contactNumber(bookingHistory.getContactNumber())
                .contactEmail(bookingHistory.getContactEmail())
                .startTime(bookingHistory.getStartTime())
                .endTime(bookingHistory.getEndTime())
                .build();
    }

    private SavedBookingCtx createBookingWithRoomId(BookingCommand command){
        // fetch conference room
        final var room= roomRepository.findById(command.roomId())
                .orElseThrow( () -> new RuntimeException("") );

        // query slots by roomId and timeRange
        final var slotList = querySlotService.query(room,
                LocalDateTime.parse(command.meetingStartTime(), dateTimeFormatter).toLocalTime(),
                LocalDateTime.parse(command.meetingEndTime(), dateTimeFormatter).toLocalTime());

        // load slots again with lock access

        // book slot
        final var updateSlotList = slotList.stream()
                .map(x -> {
                    x.setStatus(1);
                    return x;
                })
                .collect(Collectors.toList());

        // save all
        slotsRepository.saveAll(updateSlotList);

        return new SavedBookingCtx(room, updateSlotList);
    }

    private SavedBookingCtx createBookingWithOptimalSearch(BookingCommand command){

        // find optimal room base on participants
        var searchCommand = QueryOptimalRoomCommand.builder()
                .noOfParticipant(command.numberOfParticipants())
                .meetingStartTime(LocalDateTime.parse(command.meetingStartTime(), dateTimeFormatter).toLocalTime())
                .meetingEndTime(LocalDateTime.parse(command.meetingEndTime(), dateTimeFormatter).toLocalTime())
                .build();

        final var optimalRoom = queryOptimalRoomService.query(searchCommand)
                .orElseThrow(() -> new RuntimeException(""));

        // query slots by roomId and timeRange
        final var slotList = querySlotService.query(optimalRoom,
                LocalDateTime.parse(command.meetingStartTime(), dateTimeFormatter).toLocalTime(),
                LocalDateTime.parse(command.meetingEndTime(), dateTimeFormatter).toLocalTime());

        // load slots again with lock access

        // book slot
        final var updateSlotList = slotList.stream()
                .map(x -> {
                    x.setStatus(1);
                    return x;
                })
                .collect(Collectors.toList());

        // save all
        slotsRepository.saveAll(updateSlotList);

        return new SavedBookingCtx(optimalRoom, updateSlotList);
    }



    @Data
    @AllArgsConstructor
    private static class SavedBookingCtx {
        private ConferenceRoom bookedRoom;
        private List<ConferenceRoomSlots> bookedSlots;
    }
}
