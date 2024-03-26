package com.mamun25dev.crbservice.service;

import com.mamun25dev.crbservice.TestUtils;
import com.mamun25dev.crbservice.dto.command.QueryOptimalRoomCommand;
import com.mamun25dev.crbservice.repository.ConferenceRoomRepository;
import com.mamun25dev.crbservice.service.adapter.QueryOptimalRoomServiceImpl;
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
    private QuerySlotService querySlotService;

    @Mock
    private ConferenceRoomRepository conferenceRoomRepository;

    @Test
    @DisplayName("test optimal room search")
    public void test_optimal_room_search(){
        // given
        final var searchCommand = QueryOptimalRoomCommand.builder()
                .noOfParticipant(3)
                .meetingStartTime(LocalTime.of(10, 00))
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

}
