package com.mamun25dev.crbservice;

import com.mamun25dev.crbservice.domain.ConferenceRoom;
import com.mamun25dev.crbservice.domain.ConferenceRoomSlots;
import java.util.List;

public class TestUtils {

    public static List<ConferenceRoom> getRoomsWithSlots(){

        var room1 = new ConferenceRoom();
        room1.setId(1L);
        room1.setName("Amaze");
        room1.setCode("501");
        room1.setCapacity(3);
        room1.setSlotInterval(15);

        var roomSlot1 = new ConferenceRoomSlots();
        roomSlot1.setConferenceRoom(room1);
        roomSlot1.setId(1L);
        roomSlot1.setSlotTimeWindow("10:00-10:15");
        roomSlot1.setStatus(0);

        var roomSlot2 = new ConferenceRoomSlots();
        roomSlot2.setConferenceRoom(room1);
        roomSlot2.setId(2L);
        roomSlot2.setSlotTimeWindow("10:15-10:30");
        roomSlot2.setStatus(0);

        var roomSlot3 = new ConferenceRoomSlots();
        roomSlot3.setConferenceRoom(room1);
        roomSlot3.setId(3L);
        roomSlot3.setSlotTimeWindow("10:30-10:45");
        roomSlot3.setStatus(0);
        // set slots
        room1.setRoomSlots(List.of(roomSlot1, roomSlot2, roomSlot3));


        var room2 = new ConferenceRoom();
        room2.setId(2L);
        room2.setName("Beauty");
        room2.setCode("502");
        room2.setCapacity(5);
        room2.setSlotInterval(15);

        var roomSlot4 = new ConferenceRoomSlots();
        roomSlot4.setConferenceRoom(room2);
        roomSlot4.setId(3L);
        roomSlot4.setSlotTimeWindow("10:00-10:15");
        roomSlot4.setStatus(0);

        var roomSlot5 = new ConferenceRoomSlots();
        roomSlot5.setConferenceRoom(room2);
        roomSlot5.setId(3L);
        roomSlot5.setSlotTimeWindow("10:15-10:30");
        roomSlot5.setStatus(1);                             // status 1 >>> booked
        // set slots
        room2.setRoomSlots(List.of(roomSlot4, roomSlot5));

        return List.of(room1, room2);
    }

    public static List<ConferenceRoom> getRoomsWithSlots2Available(){

        var room1 = new ConferenceRoom();
        room1.setId(1L);
        room1.setName("Amaze");
        room1.setCode("501");
        room1.setCapacity(3);
        room1.setSlotInterval(15);

        var roomSlot1 = new ConferenceRoomSlots();
        roomSlot1.setConferenceRoom(room1);
        roomSlot1.setId(1L);
        roomSlot1.setSlotTimeWindow("10:00-10:15");
        roomSlot1.setStatus(0);

        var roomSlot2 = new ConferenceRoomSlots();
        roomSlot2.setConferenceRoom(room1);
        roomSlot2.setId(2L);
        roomSlot2.setSlotTimeWindow("10:15-10:30");
        roomSlot2.setStatus(0);

        var roomSlot3 = new ConferenceRoomSlots();
        roomSlot3.setConferenceRoom(room1);
        roomSlot3.setId(3L);
        roomSlot3.setSlotTimeWindow("10:30-10:45");
        roomSlot3.setStatus(0);
        // set slots
        room1.setRoomSlots(List.of(roomSlot1, roomSlot2, roomSlot3));


        var room2 = new ConferenceRoom();
        room2.setId(2L);
        room2.setName("Beauty");
        room2.setCode("502");
        room2.setCapacity(5);
        room2.setSlotInterval(15);

        var roomSlot4 = new ConferenceRoomSlots();
        roomSlot4.setConferenceRoom(room2);
        roomSlot4.setId(3L);
        roomSlot4.setSlotTimeWindow("10:00-10:15");
        roomSlot4.setStatus(0);

        var roomSlot5 = new ConferenceRoomSlots();
        roomSlot5.setConferenceRoom(room2);
        roomSlot5.setId(3L);
        roomSlot5.setSlotTimeWindow("10:15-10:30");
        roomSlot5.setStatus(0);
        // set slots
        room2.setRoomSlots(List.of(roomSlot4, roomSlot5));

        return List.of(room1, room2);
    }


    public static List<ConferenceRoom> getRoomsWithSlots_noAvailableRoom(){

        var room1 = new ConferenceRoom();
        room1.setId(1L);
        room1.setName("Amaze");
        room1.setCode("501");
        room1.setCapacity(3);
        room1.setSlotInterval(15);

        var roomSlot1 = new ConferenceRoomSlots();
        roomSlot1.setConferenceRoom(room1);
        roomSlot1.setId(1L);
        roomSlot1.setSlotTimeWindow("10:00-10:15");
        roomSlot1.setStatus(1);

        var roomSlot2 = new ConferenceRoomSlots();
        roomSlot2.setConferenceRoom(room1);
        roomSlot2.setId(2L);
        roomSlot2.setSlotTimeWindow("10:15-10:30");
        roomSlot2.setStatus(1);

        var roomSlot3 = new ConferenceRoomSlots();
        roomSlot3.setConferenceRoom(room1);
        roomSlot3.setId(3L);
        roomSlot3.setSlotTimeWindow("10:30-10:45");
        roomSlot3.setStatus(1);
        // set slots
        room1.setRoomSlots(List.of(roomSlot1, roomSlot2, roomSlot3));


        var room2 = new ConferenceRoom();
        room2.setId(2L);
        room2.setName("Beauty");
        room2.setCode("502");
        room2.setCapacity(5);
        room2.setSlotInterval(15);

        var roomSlot4 = new ConferenceRoomSlots();
        roomSlot4.setConferenceRoom(room2);
        roomSlot4.setId(3L);
        roomSlot4.setSlotTimeWindow("10:00-10:15");
        roomSlot4.setStatus(1);

        var roomSlot5 = new ConferenceRoomSlots();
        roomSlot5.setConferenceRoom(room2);
        roomSlot5.setId(3L);
        roomSlot5.setSlotTimeWindow("10:15-10:30");
        roomSlot5.setStatus(1);
        // set slots
        room2.setRoomSlots(List.of(roomSlot4, roomSlot5));

        return List.of(room1, room2);
    }

}
