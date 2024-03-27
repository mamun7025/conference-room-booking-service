package com.mamun25dev.crbservice.service;

import com.mamun25dev.crbservice.TestUtils;
import com.mamun25dev.crbservice.dto.command.QueryOptimalRoomCommand;
import com.mamun25dev.crbservice.exception.BusinessException;
import com.mamun25dev.crbservice.repository.ConferenceRoomRepository;
import com.mamun25dev.crbservice.service.adapter.QueryOptimalRoomServiceImpl;
import com.mamun25dev.crbservice.service.adapter.QuerySlotServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalTime;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class QueryOptimalRoomServiceTest {

    @InjectMocks
    private QueryOptimalRoomServiceImpl queryOptimalRoomService;

    @Spy
    private QuerySlotServiceImpl querySlotService;

    @Mock
    private ConferenceRoomRepository conferenceRoomRepository;

    @Test
    @Order(1)
    @DisplayName("optimal room search")
    public void test_optimal_room_search(){
        // given
        final var searchCommand = QueryOptimalRoomCommand.builder()
                .noOfParticipant(3)
                .meetingStartTime(LocalTime.of(10, 0))
                .meetingEndTime(LocalTime.of(10, 30))
                .build();

        final var conferenceRoomList = TestUtils.getRoomsWithSlots();
        Mockito.when(conferenceRoomRepository.findAllByCapacityGreaterThanEqual(3)).thenReturn(conferenceRoomList);

        // when
        final var response = queryOptimalRoomService.query(searchCommand);

        // then
        Assertions.assertTrue(response.isPresent());
        Assertions.assertEquals("501", response.get().getCode());
        Assertions.assertEquals("Amaze", response.get().getName());
    }

    @Test
    @Order(2)
    @DisplayName("when optimal room search with maintenance overlapped time then should fail")
    public void test_with_maintenance_overlapped_time(){
        // given
        final var searchCommand = QueryOptimalRoomCommand.builder()
                .noOfParticipant(3)
                .meetingStartTime(LocalTime.of(13, 0))
                .meetingEndTime(LocalTime.of(13, 30))
                .build();

        final var conferenceRoomList = TestUtils.getRoomsWithSlots_overlapped();
        Mockito.when(conferenceRoomRepository.findAllByCapacityGreaterThanEqual(3)).thenReturn(conferenceRoomList);


        // when
        final var exception = Assertions.assertThrows(
                BusinessException.class,
                () -> queryOptimalRoomService.query(searchCommand));


        // then
        Assertions.assertEquals("CR-0004", exception.getErrorCode());
    }

}
