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
    @Order(2)
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

}
