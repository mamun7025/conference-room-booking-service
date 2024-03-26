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


@ExtendWith(MockitoExtension.class)
public class QueryOptimalRoomServiceTest {

    @InjectMocks
    private QueryOptimalRoomServiceImpl queryOptimalRoomService;

    @Spy
    private QuerySlotServiceImpl querySlotService;

    @Mock
    private ConferenceRoomRepository conferenceRoomRepository;

    @Test
    @DisplayName("optimal room search test")
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
    @DisplayName("test with maintenance overlapped time")
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
