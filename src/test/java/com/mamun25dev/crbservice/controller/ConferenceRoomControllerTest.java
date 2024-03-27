package com.mamun25dev.crbservice.controller;

import com.mamun25dev.crbservice.dto.AvailableRoom;
import com.mamun25dev.crbservice.dto.BookingDetails;
import com.mamun25dev.crbservice.dto.LoginUser;
import com.mamun25dev.crbservice.dto.SlotInfo;
import com.mamun25dev.crbservice.dto.command.BookingCommand;
import com.mamun25dev.crbservice.dto.command.QueryCommand;
import com.mamun25dev.crbservice.dto.request.BookingRequest;
import com.mamun25dev.crbservice.service.CreateBookingService;
import com.mamun25dev.crbservice.service.QueryAvailableRoomService;
import com.mamun25dev.crbservice.utils.HttpHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ConferenceRoomControllerTest {

    @InjectMocks
    private ConferenceRoomController conferenceRoomController;

    @Mock
    private HttpHelper httpHelper;
    @Mock
    private CreateBookingService createBookingService;
    @Mock
    private QueryAvailableRoomService queryAvailableRoomService;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Test
    @DisplayName("query all available conference rooms")
    public void test_query_all_available_conference_rooms(){
        // given
        final var loginUserInfo = LoginUser.builder()
                .userId("10025")
                .mobileNumber("0563489299")
                .emailId("mamun7025@gmail.com")
                .build();
        Mockito.when(httpHelper.getLoginUserInfo()).thenReturn(loginUserInfo);

        final var queryCommand = QueryCommand.builder()
                .startTime("10:00")
                .endTime("10:30")
                .build();

        final var availableRoomList = List.of(
                AvailableRoom.builder()
                        .id(1L)
                        .name("Amaze")
                        .roomCode("501")
                        .capacity(3)
                        .slotList(List.of(SlotInfo.builder().id(5L).slotWindow("10:00-10:15").status(0).build(),
                                SlotInfo.builder().id(6L).slotWindow("10:15-10:30").status(0).build()))
                        .build());
        Mockito.when(queryAvailableRoomService.query(queryCommand)).thenReturn(availableRoomList);

        // when
        final var response = conferenceRoomController.queryAllAvailableRooms("10:00", "10:30");

        // then
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Amaze", response.getData().get(0).getName());
        Assertions.assertEquals("501", response.getData().get(0).getRoomCode());
        Assertions.assertEquals(2, response.getData().get(0).getSlotList().size());
    }

    @Test
    @DisplayName("create conference room booking")
    public void test_create_conference_room_booking(){
        // given
        final var loginUserInfo = LoginUser.builder()
                .userId("10025")
                .mobileNumber("0563489299")
                .emailId("mamun7025@gmail.com")
                .build();
        Mockito.when(httpHelper.getLoginUserInfo()).thenReturn(loginUserInfo);


        final var bookingRequest = BookingRequest.builder()
                .meetingTitle("Retro Meeting")
                .numberOfParticipants(3)
                .meetingStartTime("2024-03-26 10:00")
                .meetingEndTime("2024-03-26 10:30")
                .build();

        final var bookingCommand = BookingCommand.builder()
                .roomId(null)
                .meetingTitle("Retro Meeting")
                .numberOfParticipants(3)
                .meetingStartTime("2024-03-26 10:00")
                .meetingEndTime("2024-03-26 10:30")
                .requestorUserId("10025")
                .requestorMobileNumber("0563489299")
                .requestorEmail("mamun7025@gmail.com")
                .build();


        final var bookingDetails = BookingDetails.builder()
                .id(1L)
                .roomId(1L)
                .meetingTitle("Retro Meeting")
                .noOfParticipants(3)
                .startTime(LocalDateTime.parse("2024-03-26 10:00", dateTimeFormatter))
                .endTime(LocalDateTime.parse("2024-03-26 10:30", dateTimeFormatter))
                .createdBy("10025")
                .createdDate(Instant.now())
                .contactEmail("mamun7025@gmail.com")
                .contactNumber("0563489299")
                .build();

        Mockito.when(createBookingService.create(bookingCommand)).thenReturn(bookingDetails);


        // when
        final var response = conferenceRoomController.createBooking(bookingRequest);

        // then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(1L, response.getData().getRoomId());
        Assertions.assertEquals("Retro Meeting", response.getData().getMeetingTitle());
        Assertions.assertEquals("10025", response.getData().getCreatedBy());
    }

}
