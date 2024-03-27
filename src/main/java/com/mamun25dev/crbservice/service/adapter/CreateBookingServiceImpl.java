package com.mamun25dev.crbservice.service.adapter;

import com.mamun25dev.crbservice.domain.ConferenceRoom;
import com.mamun25dev.crbservice.domain.ConferenceRoomSlots;
import com.mamun25dev.crbservice.domain.RoomBookingHistory;
import com.mamun25dev.crbservice.dto.command.BookingCommand;
import com.mamun25dev.crbservice.dto.BookingDetails;
import com.mamun25dev.crbservice.dto.command.QueryOptimalRoomCommand;
import com.mamun25dev.crbservice.exception.BusinessException;
import com.mamun25dev.crbservice.repository.ConferenceRoomRepository;
import com.mamun25dev.crbservice.repository.ConferenceRoomSlotsRepository;
import com.mamun25dev.crbservice.repository.RoomBookingHistoryRepository;
import com.mamun25dev.crbservice.service.CreateBookingService;
import com.mamun25dev.crbservice.service.QueryOptimalRoomService;
import com.mamun25dev.crbservice.service.QuerySlotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import static com.mamun25dev.crbservice.exception.ConferenceRoomErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateBookingServiceImpl implements CreateBookingService {

    private final Clock clock;
    private final ConferenceRoomRepository roomRepository;
    private final ConferenceRoomSlotsRepository slotsRepository;
    private final RoomBookingHistoryRepository historyRepository;

    private final QuerySlotService querySlotService;
    private final QueryOptimalRoomService queryOptimalRoomService;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public BookingDetails create(BookingCommand command) {

        validateBasic(command);

        // find conference room
        final var room = (command.roomId() != null)
                ? queryRoomById(command.roomId())
                : queryOptimalRoom(command);


        // query slots by roomId and timeRange
        final var slotList = querySlotService.query(room,
                LocalDateTime.parse(command.meetingStartTime(), dateTimeFormatter).toLocalTime(),
                LocalDateTime.parse(command.meetingEndTime(), dateTimeFormatter).toLocalTime());


        final var bookSlotIds = slotList.stream().map(x -> x.getId()).collect(Collectors.toList());
        // execute booking with lock
        final var bookedSlotList = execute(bookSlotIds);


        var bookedSlotIds = bookedSlotList.stream()
                .map(x -> x.getId().toString())
                .collect(Collectors.toList());


        // insert into history
        final var bookingHistory = insertIntoBookingHistory(command, room, bookedSlotIds);

        // response
        return BookingDetails.builder()
                .id(bookingHistory.getId())
                .roomId(bookingHistory.getConferenceRoom().getId())
                .roomName(bookingHistory.getConferenceRoom().getName())
                .slotIds(bookedSlotIds)
                .noOfParticipants(bookingHistory.getNoOfParticipants())
                .meetingTitle(bookingHistory.getMeetingTitle())
                .contactNumber(bookingHistory.getContactNumber())
                .contactEmail(bookingHistory.getContactEmail())
                .startTime(bookingHistory.getStartTime())
                .endTime(bookingHistory.getEndTime())
                .createdBy(command.requestorUserId())
                .createdDate(bookingHistory.getCreatedDate())
                .build();
    }


    private ConferenceRoom queryRoomById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new BusinessException(CONFERENCE_ROOM_NOT_FOUND));
    }

    private ConferenceRoom queryOptimalRoom(BookingCommand command) {
        // search optimal room base on participants
        var searchCommand = QueryOptimalRoomCommand.builder()
                .noOfParticipant(command.numberOfParticipants())
                .meetingStartTime(LocalDateTime.parse(command.meetingStartTime(), dateTimeFormatter).toLocalTime())
                .meetingEndTime(LocalDateTime.parse(command.meetingEndTime(), dateTimeFormatter).toLocalTime())
                .build();

        final var optimalRoom = queryOptimalRoomService.query(searchCommand)
                .orElseThrow(() -> new BusinessException(CONFERENCE_ROOM_NOT_AVAILABLE_IN_RANGE));
        log.info("optimal room: {}", optimalRoom.getName());

        return optimalRoom;
    }



    public List<ConferenceRoomSlots> execute(List<Long> slotIds){

        // read slots from database with lock access
        final var slotList = slotsRepository.findAllSlotsByIdList(slotIds);
        log.info("loaded slots with write access: {}", slotList);

        // check slot is book meanwhile???
        //  1 = booked
        // -1 = maintenance time
        slotList.stream()
                .filter(slot -> slot.getStatus() == 1 || slot.getStatus() == -1)
                .findAny()
                .ifPresent(x -> {
                    throw new BusinessException(CONFERENCE_ROOM_SLOT_BOOKED_ALREADY);
                });


        // change slot status = 1/booked
        final var bookSlotList = slotList.stream()
                .map(x -> {
                    x.setStatus(1);
                    return x;
                })
                .collect(Collectors.toList());

        // save all
        return slotsRepository.saveAll(bookSlotList);
    }


    private RoomBookingHistory insertIntoBookingHistory(BookingCommand command,
                                                        ConferenceRoom room,
                                                        List<String> bookedSlotIds) {
        var bookingHistory = new RoomBookingHistory();
        bookingHistory.setConferenceRoom(room);
        bookingHistory.setMeetingTitle(command.meetingTitle());
        bookingHistory.setNoOfParticipants(command.numberOfParticipants());
        bookingHistory.setStartTime(LocalDateTime.parse(command.meetingStartTime(), dateTimeFormatter));
        bookingHistory.setEndTime(LocalDateTime.parse(command.meetingEndTime(), dateTimeFormatter));
        bookingHistory.setContactNumber(command.requestorMobileNumber());
        bookingHistory.setContactEmail(command.requestorEmail());
        bookingHistory.setCreatedBy(command.requestorUserId());
        bookingHistory.setSlotIds(bookedSlotIds);
        var savedBooking = historyRepository.save(bookingHistory);
        log.info("saved booking id: {}", savedBooking.getId());

        return savedBooking;
    }


    private void validateBasic(BookingCommand command) {
        final var meetingStartDateTime = LocalDateTime.parse(command.meetingStartTime(), dateTimeFormatter);
        final var meetingEndDateTime = LocalDateTime.parse(command.meetingEndTime(), dateTimeFormatter);

        final var todayDate = LocalDate.now(clock);
        if(todayDate.compareTo(meetingStartDateTime.toLocalDate()) != 0
                || todayDate.compareTo(meetingEndDateTime.toLocalDate()) != 0)
            throw new BusinessException(BOOKING_DATE_VALIDATION_FAILURE);

        // past time
        final var todayTime = LocalTime.now(clock);
        if(todayTime.isAfter(meetingStartDateTime.toLocalTime())
                || todayTime.isAfter(meetingEndDateTime.toLocalTime()))
            throw new BusinessException(BOOKING_TIME_VALIDATION_FAILURE);

        if(meetingStartDateTime.toLocalTime().isAfter(meetingEndDateTime.toLocalTime()))
            throw new BusinessException(START_TIME_END_TIME_FAILURE);
    }

}
