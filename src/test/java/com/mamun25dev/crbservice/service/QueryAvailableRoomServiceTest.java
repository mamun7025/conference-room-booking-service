package com.mamun25dev.crbservice.service;

import com.mamun25dev.crbservice.TestUtils;
import com.mamun25dev.crbservice.domain.ConferenceRoom;
import com.mamun25dev.crbservice.dto.command.QueryCommand;
import com.mamun25dev.crbservice.exception.BusinessException;
import com.mamun25dev.crbservice.repository.ConferenceRoomRepository;
import com.mamun25dev.crbservice.service.adapter.QueryAvailableRoomServiceImpl;
import com.mamun25dev.crbservice.service.adapter.QuerySlotServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QueryAvailableRoomServiceTest {

    @InjectMocks
    private QueryAvailableRoomServiceImpl queryAvailableRoomService;

    @Mock
    private ConferenceRoomRepository conferenceRoomRepository;

    @Spy
    private QuerySlotServiceImpl querySlotService;

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
        ReflectionTestUtils.setField(queryAvailableRoomService, "clock", clock);
    }


    @Test
    @Order(1)
    @DisplayName("when query available room by past time then should fail")
    public void when_query_available_room_by_past_time_then_should_fail(){
        // given
        final var queryCommand = QueryCommand.builder()
                .startTime("09:30")
                .endTime("09:45")
                .build();

        // when
        final var exception = Assertions.assertThrows(
                BusinessException.class,
                () -> queryAvailableRoomService.query(queryCommand));


        // then
        Assertions.assertEquals("CR-0006", exception.getErrorCode());
    }


    @Test
    @Order(2)
    @DisplayName("when end time less than start time then should fail")
    public void when_end_time_less_than_start_time_then_should_fail(){
        // given
        final var queryCommand = QueryCommand.builder()
                .startTime("11:45")
                .endTime("11:15")
                .build();

        // when
        final var exception = Assertions.assertThrows(
                BusinessException.class,
                () -> queryAvailableRoomService.query(queryCommand));


        // then
        Assertions.assertEquals("CR-0007", exception.getErrorCode());
    }


    @Test
    @Order(3)
    @DisplayName("when available room not found then should fail")
    public void test_query_available_conference_room_return_no_room(){
        // given
        final var queryCommand = QueryCommand.builder()
                .startTime("10:00")
                .endTime("10:30")
                .build();

        List<ConferenceRoom> roomList = TestUtils.getRoomsWithSlots_noAvailableRoom();
        Mockito.when(conferenceRoomRepository.findAll()).thenReturn(roomList);

        // when
        final var exception = Assertions.assertThrows(
                BusinessException.class,
                () -> queryAvailableRoomService.query(queryCommand));


        // then
        Assertions.assertEquals("CR-0002", exception.getErrorCode());
    }






    @Test
    @Order(4)
    @DisplayName("query available conference room - return 1 room")
    public void test_query_available_conference_room1(){
        // given
        final var queryCommand = QueryCommand.builder()
                .startTime("10:00")
                .endTime("10:30")
                .build();

        List<ConferenceRoom> roomList = TestUtils.getRoomsWithSlots();
        Mockito.when(conferenceRoomRepository.findAll()).thenReturn(roomList);

        // when
        final var response = queryAvailableRoomService.query(queryCommand);


        // then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.size());
    }


    @Test
    @Order(5)
    @DisplayName("query available conference room - return 2 room")
    public void test_query_available_conference_room2(){
        // given
        final var queryCommand = QueryCommand.builder()
                .startTime("10:00")
                .endTime("10:30")
                .build();

        List<ConferenceRoom> roomList = TestUtils.getRoomsWithSlots2Available();
        Mockito.when(conferenceRoomRepository.findAll()).thenReturn(roomList);

        // when
        final var response = queryAvailableRoomService.query(queryCommand);


        // then
        Assertions.assertNotNull(response);
        Assertions.assertEquals(2, response.size());
    }

    @Test
    @Order(6)
    @DisplayName("when query time range overlapped with maintenance time then should fail")
    public void when_query_time_range_overlapped_with_maintenance_time_then_should_fail(){
        // given
        final var queryCommand = QueryCommand.builder()
                .startTime("13:00")
                .endTime("13:30")
                .build();

        List<ConferenceRoom> roomList = TestUtils.getRoomsWithSlots_overlapped();
        Mockito.when(conferenceRoomRepository.findAll()).thenReturn(roomList);

        // when
        final var exception = Assertions.assertThrows(
                BusinessException.class,
                () -> queryAvailableRoomService.query(queryCommand));


        // then
        Assertions.assertEquals("CR-0004", exception.getErrorCode());
    }

}
