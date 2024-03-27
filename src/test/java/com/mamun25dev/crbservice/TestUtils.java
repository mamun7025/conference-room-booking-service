package com.mamun25dev.crbservice;

import com.mamun25dev.crbservice.domain.ConferenceRoom;
import com.mamun25dev.crbservice.domain.ConferenceRoomSlots;
import org.apache.commons.lang3.StringUtils;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestUtils {

    private static final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HHmm");
    private static final DateTimeFormatter timeFormat2 = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    /**
     * For live testing
     * Better use fixed clock for testing
     * @return
     */
    public static Map<String, String> meetingTimeSeed(){
        var dateTimeNow = LocalDateTime.now(ZoneId.systemDefault());
        var timeNow = dateTimeNow.toLocalTime();

        var startTime = StringUtils.EMPTY;
        var endTime = StringUtils.EMPTY;

        if(timeNow.getMinute() < 15){
            startTime = String.format("%s %s:%s", dateTimeNow.format(dateFormatter), timeNow.getHour(), "15");
            endTime = String.format("%s %s:%s", dateTimeNow.format(dateFormatter), timeNow.getHour(), "45");
        } else if(timeNow.getMinute() < 30){
            startTime = String.format("%s %s:%s", dateTimeNow.format(dateFormatter), timeNow.getHour(), "30");
            endTime = String.format("%s %s:%s", dateTimeNow.format(dateFormatter), timeNow.getHour(), "45");
        } else if(timeNow.getMinute() < 45){
            startTime = String.format("%s %s:%s", dateTimeNow.format(dateFormatter), timeNow.getHour(), "45");
            endTime = String.format("%s %s:%s", dateTimeNow.format(dateFormatter), timeNow.getHour() + 1, "00");
        } else {
            startTime = String.format("%s %s:%s", dateTimeNow.format(dateFormatter), timeNow.getHour() + 1, "00");
            endTime = String.format("%s %s:%s", dateTimeNow.format(dateFormatter), timeNow.getHour() + 1, "15");
        }
        String finalStartTime = startTime;
        String finalEndTime = endTime;
        return new HashMap<>(){{
            put("startTime", finalStartTime);
            put("endTime", finalEndTime);
        }};
    }


    public static List<ConferenceRoom> getRoomsWithSlots(){

        // room 1 -----------------------------------------------
        var room1 = new ConferenceRoom();
        room1.setId(1L);
        room1.setName("Amaze");
        room1.setCode("501");
        room1.setCapacity(3);
        room1.setSlotInterval(15);
        room1.setWorkingHourWindow("09:00-18:00");

        var roomSlot1 = new ConferenceRoomSlots();
        roomSlot1.setConferenceRoom(room1);
        roomSlot1.setId(1L);
        roomSlot1.setSlotTimeWindow("09:00-09:15");
        roomSlot1.setStatus(0);

        var roomSlot2 = new ConferenceRoomSlots();
        roomSlot2.setConferenceRoom(room1);
        roomSlot2.setId(2L);
        roomSlot2.setSlotTimeWindow("09:15-09:30");
        roomSlot2.setStatus(0);

        var roomSlot3 = new ConferenceRoomSlots();
        roomSlot3.setConferenceRoom(room1);
        roomSlot3.setId(3L);
        roomSlot3.setSlotTimeWindow("09:30-09:45");
        roomSlot3.setStatus(0);
        // room1.setRoomSlots(List.of(roomSlot1, roomSlot2, roomSlot3));


        int startKeyLkp = Integer.parseInt("0945");     // 09:45 AM
        int endKeyLkp = Integer.parseInt("2345");       // 18:00 PM
        int keyInterval = 15;

        List<ConferenceRoomSlots> room1SlotList = new ArrayList<>();
        room1SlotList.add(roomSlot1);
        room1SlotList.add(roomSlot2);
        room1SlotList.add(roomSlot3);

        long idCount = 4;
        for (int lkpKey = startKeyLkp; lkpKey < endKeyLkp; ){
            var startTime = LocalTime.parse(String.format("%04d", lkpKey), timeFormat);
            var endTime = startTime.plusMinutes(keyInterval);

            var roomSlotDynamic = new ConferenceRoomSlots();
            roomSlotDynamic.setConferenceRoom(room1);
            roomSlotDynamic.setId(idCount);
            roomSlotDynamic.setSlotTimeWindow(String.format("%s-%s", startTime.format(timeFormat2), endTime.format(timeFormat2)));
            roomSlotDynamic.setStatus(0);
            room1SlotList.add(roomSlotDynamic);

            idCount++;
            lkpKey = getNextLookupKey(keyInterval, lkpKey);
        }
        // set slots
        room1.setRoomSlots(room1SlotList);


        // room 2 -----------------------------------------------
        var room2 = new ConferenceRoom();
        room2.setId(2L);
        room2.setName("Beauty");
        room2.setCode("502");
        room2.setCapacity(5);
        room2.setSlotInterval(15);
        room2.setWorkingHourWindow("09:00-18:00");

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

    public static List<ConferenceRoom> getRoomsWithSlots_overlapped(){

        var room1 = new ConferenceRoom();
        room1.setId(1L);
        room1.setName("Amaze");
        room1.setCode("501");
        room1.setCapacity(3);
        room1.setSlotInterval(15);

        var roomSlot1 = new ConferenceRoomSlots();
        roomSlot1.setConferenceRoom(room1);
        roomSlot1.setId(1L);
        roomSlot1.setSlotTimeWindow("13:00-13:15");
        roomSlot1.setStatus(-1);

        var roomSlot2 = new ConferenceRoomSlots();
        roomSlot2.setConferenceRoom(room1);
        roomSlot2.setId(2L);
        roomSlot2.setSlotTimeWindow("13:15-13:30");
        roomSlot2.setStatus(0);

        var roomSlot3 = new ConferenceRoomSlots();
        roomSlot3.setConferenceRoom(room1);
        roomSlot3.setId(3L);
        roomSlot3.setSlotTimeWindow("13:30-13:45");
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


    public static List<ConferenceRoomSlots> getConferenceRoomSlots(){

        var room1 = new ConferenceRoom();
        room1.setId(1L);
        room1.setName("Amaze");
        room1.setCode("501");
        room1.setCapacity(3);
        room1.setSlotInterval(15);

        var roomSlot1 = new ConferenceRoomSlots();
        roomSlot1.setConferenceRoom(room1);
        roomSlot1.setId(1L);
        roomSlot1.setSlotTimeWindow("09:00-09:15");
        roomSlot1.setStatus(0);

        var roomSlot2 = new ConferenceRoomSlots();
        roomSlot2.setConferenceRoom(room1);
        roomSlot2.setId(2L);
        roomSlot2.setSlotTimeWindow("09:15-09:30");
        roomSlot2.setStatus(0);

        var roomSlot3 = new ConferenceRoomSlots();
        roomSlot3.setConferenceRoom(room1);
        roomSlot3.setId(3L);
        roomSlot3.setSlotTimeWindow("09:30-09:45");
        roomSlot3.setStatus(0);


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

        return List.of(roomSlot1, roomSlot2, roomSlot3, roomSlot4, roomSlot5);
    }


    public static List<ConferenceRoomSlots> getRoom1AvailableSlots(){

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

        return List.of(roomSlot1, roomSlot2, roomSlot3, roomSlot4, roomSlot5);
    }

    public static List<ConferenceRoomSlots> getRoom1BookedSlots(){

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
        roomSlot2.setStatus(1);

        var roomSlot3 = new ConferenceRoomSlots();
        roomSlot3.setConferenceRoom(room1);
        roomSlot3.setId(3L);
        roomSlot3.setSlotTimeWindow("10:30-10:45");
        roomSlot3.setStatus(0);

        return List.of(roomSlot1, roomSlot2, roomSlot3);
    }


    private static int getNextLookupKey(int keyInterval, int lkpKey) {
        var lkpKeyTime = LocalTime.parse(String.format("%04d", lkpKey), timeFormat);
        return Integer.parseInt(lkpKeyTime.plusMinutes(keyInterval).format(timeFormat));
    }

}
