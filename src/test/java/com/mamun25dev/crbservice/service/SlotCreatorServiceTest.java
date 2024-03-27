package com.mamun25dev.crbservice.service;

import com.mamun25dev.crbservice.TestUtils;
import com.mamun25dev.crbservice.domain.MaintenanceSettings;
import com.mamun25dev.crbservice.repository.ConferenceRoomRepository;
import com.mamun25dev.crbservice.repository.ConferenceRoomSlotsRepository;
import com.mamun25dev.crbservice.repository.MaintenanceSettingsRepository;
import com.mamun25dev.crbservice.service.adapter.QuerySlotServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;


@ExtendWith(MockitoExtension.class)
public class SlotCreatorServiceTest {

    @InjectMocks
    private SlotCreatorService slotCreatorService;

    @Mock
    private ConferenceRoomRepository conferenceRoomRepository;
    @Mock
    private ConferenceRoomSlotsRepository conferenceRoomSlotsRepository;
    @Mock
    private MaintenanceSettingsRepository maintenanceSettingsRepository;
    @Spy
    private QuerySlotServiceImpl querySlotService;

    @Test
    @DisplayName("create slots for room1 and room2")
    public void test_create_slot_for_room1_and_room2(){
        // given
        final var roomList = TestUtils.getRoomsWithSlots();
        Mockito.when(conferenceRoomRepository.findAll()).thenReturn(roomList);

        final var slotList = TestUtils.getConferenceRoomSlots();
        Mockito.when(conferenceRoomSlotsRepository.saveAll(Mockito.anyList())).thenReturn(slotList);

        final var maintenanceSettings = new MaintenanceSettings();
        maintenanceSettings.setConferenceRoom(roomList.get(0));
        maintenanceSettings.setMaintenanceSlot("09:00-09:15");
        Mockito.when(maintenanceSettingsRepository.findAllByConferenceRoom(roomList.get(0))).thenReturn(List.of(maintenanceSettings));


        // when
        slotCreatorService.createAllConferenceRoomSlots();

        // then
        Mockito.verify(conferenceRoomRepository, Mockito.times(2)).findAll();
        Mockito.verify(conferenceRoomSlotsRepository, Mockito.times(3)).saveAll(Mockito.anyList());
    }

}
