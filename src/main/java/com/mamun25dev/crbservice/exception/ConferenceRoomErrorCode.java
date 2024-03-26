package com.mamun25dev.crbservice.exception;

import lombok.Getter;

@Getter
public enum ConferenceRoomErrorCode {

    CONFERENCE_ROOM_NOT_FOUND("CR-0001", "Conference room not found by id"),
    CONFERENCE_ROOM_NOT_AVAILABLE_IN_RANGE("CR-0002", "Conference room not available in query range"),
    CONFERENCE_ROOM_SLOT_BOOKED_ALREADY("CR-0003", "Someone already booked slot, please try with other time range"),
    OVERLAP_WITH_MAINTENANCE_FAILURE("CR-0004", "Booking can not be done during maintenance time"),
    BOOKING_DATE_VALIDATION_FAILURE("CR-0005", "Booking only allowed in current date"),
    BOOKING_TIME_VALIDATION_FAILURE("CR-0006", "Past time not allowed"),
    START_TIME_END_TIME_FAILURE("CR-0007", "Start time should be lesser than end time"),
    ;


    private String errorCode;
    private String errorMessage;


    ConferenceRoomErrorCode(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
