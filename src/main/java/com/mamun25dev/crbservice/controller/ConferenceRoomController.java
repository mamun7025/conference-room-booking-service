package com.mamun25dev.crbservice.controller;

import com.mamun25dev.crbservice.dto.command.BookingCommand;
import com.mamun25dev.crbservice.dto.BookingDetails;
import com.mamun25dev.crbservice.dto.command.QueryCommand;
import com.mamun25dev.crbservice.dto.response.ResponseModel;
import com.mamun25dev.crbservice.dto.response.ResponseStatus;
import com.mamun25dev.crbservice.dto.request.BookingRequest;
import com.mamun25dev.crbservice.dto.AvailableRoom;
import com.mamun25dev.crbservice.service.CreateBookingService;
import com.mamun25dev.crbservice.service.QueryAvailableRoomService;
import com.mamun25dev.crbservice.utils.HttpHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/conference-room")
public class ConferenceRoomController {

    private final HttpHelper httpHelper;
    private final CreateBookingService createBookingService;
    private final QueryAvailableRoomService queryAvailableRoomService;



    @GetMapping("/available")
    public ResponseModel<List<AvailableRoom>> queryAllAvailableRooms(@RequestParam(name = "startTime") String startTime,
                                                                     @RequestParam(name = "endTime") String endTime){
        final var loginUser = httpHelper.getLoginUserInfo();
        log.info("query request come from user: {} mobile number: {}",
                loginUser.getUserId(), loginUser.getMobileNumber());

        var queryCommand = QueryCommand.builder()
                .startTime(startTime)
                .endTime(endTime)
                .build();
        final var availableRoomList = queryAvailableRoomService.query(queryCommand);

        return ResponseModel.<List<AvailableRoom>>builder()
                .status(ResponseStatus.SUCCESS)
                .data(availableRoomList)
                .build();
    }

    @PostMapping("/booking")
    public ResponseModel<BookingDetails> createBooking(@Valid @RequestBody BookingRequest request){
        final var loginUser = httpHelper.getLoginUserInfo();
        log.info("booking request come from user: {} mobile number: {}",
                loginUser.getUserId(), loginUser.getMobileNumber());

        var bookingCommand = BookingCommand.builder()
                .roomId(request.getRoomId())
                .meetingTitle(request.getMeetingTitle())
                .numberOfParticipants(request.getNumberOfParticipants())
                .meetingStartTime(request.getMeetingStartTime())
                .meetingEndTime(request.getMeetingEndTime())
                .requestorUserId(loginUser.getUserId())
                .requestorMobileNumber(loginUser.getMobileNumber())
                .requestorEmail(loginUser.getEmailId())
                .build();
        final var bookingDetails = createBookingService.create(bookingCommand);

        return ResponseModel.<BookingDetails>builder()
                .status(ResponseStatus.SUCCESS)
                .data(bookingDetails)
                .build();
    }

}
