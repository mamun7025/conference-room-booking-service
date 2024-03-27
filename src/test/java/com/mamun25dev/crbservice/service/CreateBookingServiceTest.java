package com.mamun25dev.crbservice.service;

import com.mamun25dev.crbservice.TestUtils;
import com.mamun25dev.crbservice.domain.RoomBookingHistory;
import com.mamun25dev.crbservice.dto.command.BookingCommand;
import com.mamun25dev.crbservice.dto.command.QueryOptimalRoomCommand;
import com.mamun25dev.crbservice.exception.BusinessException;
import com.mamun25dev.crbservice.repository.ConferenceRoomRepository;
import com.mamun25dev.crbservice.repository.ConferenceRoomSlotsRepository;
import com.mamun25dev.crbservice.repository.RoomBookingHistoryRepository;
import com.mamun25dev.crbservice.service.adapter.CreateBookingServiceImpl;
import com.mamun25dev.crbservice.service.adapter.QueryOptimalRoomServiceImpl;
import com.mamun25dev.crbservice.service.adapter.QuerySlotServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@Slf4j
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateBookingServiceTest {

    @InjectMocks
    private CreateBookingServiceImpl createBookingService;

    @Mock
    private ConferenceRoomRepository roomRepository;
    @Mock
    private ConferenceRoomSlotsRepository slotsRepository;
    @Mock
    private RoomBookingHistoryRepository historyRepository;

    @Spy
    private QuerySlotServiceImpl querySlotService;
    @Mock
    private QueryOptimalRoomServiceImpl queryOptimalRoomService;


    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static final ZonedDateTime NOW = ZonedDateTime.of(
            2024,
            3,
            26,
            10,
            0,
            0,
            0,
            ZoneId.of("Asia/Dubai")
    );

    @BeforeEach
    public void setup(){
        final var clock = Clock.fixed(NOW.toInstant(), ZoneId.of("Asia/Dubai"));
        ReflectionTestUtils.setField(createBookingService, "clock", clock);
    }



    @Test
    @Order(1)
    @DisplayName(("when place booking with future time then should fail"))
    public void when_place_booking_with_future_time_then_should_fail(){
        // given
        final var bookingCommand = BookingCommand.builder()
                .roomId(1L)
                .meetingTitle("Retro Meeting")
                .numberOfParticipants(3)
                .meetingStartTime("2024-03-27 09:30")
                .meetingEndTime("2024-03-27 10:00")
                .requestorUserId("10025")
                .requestorMobileNumber("0563489299")
                .requestorEmail("mamun7025@gmail.com")
                .build();

        // when
        final var exception = Assertions.assertThrows(
                BusinessException.class,
                () -> createBookingService.create(bookingCommand));


        // then
        Assertions.assertEquals("CR-0005", exception.getErrorCode());
    }

    @Test
    @Order(2)
    @DisplayName(("when place booking with past time then should fail"))
    public void when_place_booking_with_past_time_then_should_fail(){
        // given
        final var bookingCommand = BookingCommand.builder()
                .roomId(1L)
                .meetingTitle("Retro Meeting")
                .numberOfParticipants(3)
                .meetingStartTime("2024-03-26 09:30")
                .meetingEndTime("2024-03-26 10:00")
                .requestorUserId("10025")
                .requestorMobileNumber("0563489299")
                .requestorEmail("mamun7025@gmail.com")
                .build();

        // when
        final var exception = Assertions.assertThrows(
                BusinessException.class,
                () -> createBookingService.create(bookingCommand));


        // then
        Assertions.assertEquals("CR-0006", exception.getErrorCode());
    }


    @Test
    @Order(3)
    @DisplayName(("when meeting end time less than start time then should fail"))
    public void when_meeting_end_time_less_than_start_time_then_should_fail(){
        // given
        final var bookingCommand = BookingCommand.builder()
                .roomId(1L)
                .meetingTitle("Retro Meeting")
                .numberOfParticipants(3)
                .meetingStartTime("2024-03-26 12:00")
                .meetingEndTime("2024-03-26 11:30")
                .requestorUserId("10025")
                .requestorMobileNumber("0563489299")
                .requestorEmail("mamun7025@gmail.com")
                .build();

        // when
        final var exception = Assertions.assertThrows(
                BusinessException.class,
                () -> createBookingService.create(bookingCommand));


        // then
        Assertions.assertEquals("CR-0007", exception.getErrorCode());
    }


    @Test
    @Order(4)
    @DisplayName(("when place booking in available slot time then should success"))
    public void when_place_booking_in_ideal_time_then_should_success(){
        // given
        final var bookingCommand = BookingCommand.builder()
                .meetingTitle("Retro Meeting")
                .numberOfParticipants(3)
                .meetingStartTime("2024-03-26 10:00")
                .meetingEndTime("2024-03-26 10:30")
                .requestorUserId("10025")
                .requestorMobileNumber("0563489299")
                .requestorEmail("mamun7025@gmail.com")
                .build();

        final var roomList = TestUtils.getRoomsWithSlots();

        var searchCommand = QueryOptimalRoomCommand.builder()
                .noOfParticipant(bookingCommand.numberOfParticipants())
                .meetingStartTime(LocalDateTime.parse(bookingCommand.meetingStartTime(), dateTimeFormatter).toLocalTime())
                .meetingEndTime(LocalDateTime.parse(bookingCommand.meetingEndTime(), dateTimeFormatter).toLocalTime())
                .build();
        Mockito.when(queryOptimalRoomService.query(searchCommand)).thenReturn(Optional.of(roomList.get(0)));

        final var roomSlots = TestUtils.getRoom1AvailableSlots();
        Mockito.when(slotsRepository.findAllSlotsByIdList(Mockito.anyList())).thenReturn(roomSlots);
        Mockito.when(slotsRepository.saveAll(roomSlots)).thenReturn(roomSlots);


        var bookingHistory = new RoomBookingHistory();
        bookingHistory.setConferenceRoom(roomList.get(0));
        bookingHistory.setMeetingTitle(bookingCommand.meetingTitle());
        bookingHistory.setNoOfParticipants(bookingCommand.numberOfParticipants());
        bookingHistory.setStartTime(LocalDateTime.parse(bookingCommand.meetingStartTime(), dateTimeFormatter));
        bookingHistory.setEndTime(LocalDateTime.parse(bookingCommand.meetingEndTime(), dateTimeFormatter));
        bookingHistory.setContactNumber(bookingCommand.requestorMobileNumber());
        bookingHistory.setContactEmail(bookingCommand.requestorEmail());
        bookingHistory.setSlotIds(List.of("1", "2"));
        Mockito.when(historyRepository.save(Mockito.any(RoomBookingHistory.class))).thenReturn(bookingHistory);

        // when
        final var response = createBookingService.create(bookingCommand);


        // then
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Retro Meeting", response.getMeetingTitle());
        Assertions.assertEquals(3, response.getNoOfParticipants());
    }


    @Test
    @Order(5)
    @DisplayName(("when booking time overlap with maintenance time then should fail"))
    public void when_booking_time_overlap_with_maintenance_time_then_should_fail(){
        // given
        final var bookingCommand = BookingCommand.builder()
                .meetingTitle("Retro Meeting")
                .numberOfParticipants(3)
                .meetingStartTime("2024-03-26 13:00")
                .meetingEndTime("2024-03-26 13:15")
                .requestorUserId("10025")
                .requestorMobileNumber("0563489299")
                .requestorEmail("mamun7025@gmail.com")
                .build();

        final var roomList = TestUtils.getRoomsWithSlots();

        var searchCommand = QueryOptimalRoomCommand.builder()
                .noOfParticipant(bookingCommand.numberOfParticipants())
                .meetingStartTime(LocalDateTime.parse(bookingCommand.meetingStartTime(), dateTimeFormatter).toLocalTime())
                .meetingEndTime(LocalDateTime.parse(bookingCommand.meetingEndTime(), dateTimeFormatter).toLocalTime())
                .build();
        Mockito.when(queryOptimalRoomService.query(searchCommand)).thenReturn(Optional.of(roomList.get(0)));

        final var roomSlots = TestUtils.getRoom1AvailableSlots();
        Mockito.when(slotsRepository.findAllSlotsByIdList(Mockito.anyList())).thenReturn(roomSlots);
        Mockito.when(slotsRepository.saveAll(roomSlots)).thenReturn(roomSlots);


        var bookingHistory = new RoomBookingHistory();
        bookingHistory.setConferenceRoom(roomList.get(0));
        bookingHistory.setMeetingTitle(bookingCommand.meetingTitle());
        bookingHistory.setNoOfParticipants(bookingCommand.numberOfParticipants());
        bookingHistory.setStartTime(LocalDateTime.parse(bookingCommand.meetingStartTime(), dateTimeFormatter));
        bookingHistory.setEndTime(LocalDateTime.parse(bookingCommand.meetingEndTime(), dateTimeFormatter));
        bookingHistory.setContactNumber(bookingCommand.requestorMobileNumber());
        bookingHistory.setContactEmail(bookingCommand.requestorEmail());
        bookingHistory.setSlotIds(List.of("1", "2"));
        Mockito.when(historyRepository.save(Mockito.any(RoomBookingHistory.class))).thenReturn(bookingHistory);

        // when
        final var response = createBookingService.create(bookingCommand);


        // then
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Retro Meeting", response.getMeetingTitle());
        Assertions.assertEquals(3, response.getNoOfParticipants());
    }


    @Test
    @Order(6)
    @DisplayName(("when place booking with room_id in available slot time then should success"))
    public void when_place_booking_with_room_id_in_ideal_time_then_should_success(){
        // given
        final var bookingCommand = BookingCommand.builder()
                .roomId(1L)
                .meetingTitle("Retro Meeting")
                .numberOfParticipants(3)
                .meetingStartTime("2024-03-26 10:00")
                .meetingEndTime("2024-03-26 10:30")
                .requestorUserId("10025")
                .requestorMobileNumber("0563489299")
                .requestorEmail("mamun7025@gmail.com")
                .build();

        final var roomList = TestUtils.getRoomsWithSlots();
        Mockito.when(roomRepository.findById(1L)).thenReturn(Optional.of(roomList.get(0)));

        final var roomSlots = TestUtils.getRoom1AvailableSlots();
        Mockito.when(slotsRepository.findAllSlotsByIdList(Mockito.anyList())).thenReturn(roomSlots);
        Mockito.when(slotsRepository.saveAll(roomSlots)).thenReturn(roomSlots);


        var bookingHistory = new RoomBookingHistory();
        bookingHistory.setConferenceRoom(roomList.get(0));
        bookingHistory.setMeetingTitle(bookingCommand.meetingTitle());
        bookingHistory.setNoOfParticipants(bookingCommand.numberOfParticipants());
        bookingHistory.setStartTime(LocalDateTime.parse(bookingCommand.meetingStartTime(), dateTimeFormatter));
        bookingHistory.setEndTime(LocalDateTime.parse(bookingCommand.meetingEndTime(), dateTimeFormatter));
        bookingHistory.setContactNumber(bookingCommand.requestorMobileNumber());
        bookingHistory.setContactEmail(bookingCommand.requestorEmail());
        bookingHistory.setSlotIds(List.of("1", "2"));
        Mockito.when(historyRepository.save(Mockito.any(RoomBookingHistory.class))).thenReturn(bookingHistory);

        // when
        final var response = createBookingService.create(bookingCommand);


        // then
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Retro Meeting", response.getMeetingTitle());
        Assertions.assertEquals(3, response.getNoOfParticipants());
    }


    @Test
    @Order(7)
    @DisplayName(("when concurrent booking with already booked slot then should fail"))
    public void when_concurrent_booking_with_already_booked_slot_then_should_fail(){
        // given
        final var bookingCommand = BookingCommand.builder()
                .roomId(1L)
                .meetingTitle("Retro Meeting")
                .numberOfParticipants(3)
                .meetingStartTime("2024-03-26 10:00")
                .meetingEndTime("2024-03-26 10:30")
                .requestorUserId("10025")
                .requestorMobileNumber("0563489299")
                .requestorEmail("mamun7025@gmail.com")
                .build();

        final var roomList = TestUtils.getRoomsWithSlots();
        Mockito.when(roomRepository.findById(1L)).thenReturn(Optional.of(roomList.get(0)));

        final var roomSlots = TestUtils.getRoom1BookedSlots();
        Mockito.when(slotsRepository.findAllSlotsByIdList(Mockito.anyList())).thenReturn(roomSlots);


        // when
        final var exception = Assertions.assertThrows(
                BusinessException.class,
                () -> createBookingService.create(bookingCommand));


        // then
        Assertions.assertEquals("CR-0003", exception.getErrorCode());
    }

}
