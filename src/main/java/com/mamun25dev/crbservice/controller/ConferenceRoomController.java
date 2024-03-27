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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@Slf4j
@Tag(name = "Conference Room",
        description = "Conference room management APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/conference-room")
public class ConferenceRoomController {

    private final HttpHelper httpHelper;
    private final CreateBookingService createBookingService;
    private final QueryAvailableRoomService queryAvailableRoomService;



    @Operation(summary = "Query available conference room by time range")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "available conference room list",
                    content = { @Content(mediaType = "application/json") })})
    @Parameters({
            @Parameter(name = "X-USER-ID", description = "user id",
                    in = ParameterIn.HEADER, schema = @Schema(type = "string")),
            @Parameter(name = "X-USER-MOBILE", description = "user mobile number",
                    in = ParameterIn.HEADER, schema = @Schema(type = "string"))
    })
    @GetMapping("/available")
    public ResponseModel<List<AvailableRoom>> queryAllAvailableRooms(@RequestParam(name = "startTime") String startTime,
                                                                     @RequestParam(name = "endTime") String endTime){
        final var loginUser = httpHelper.getLoginUserInfo();
        log.info("query request come from user: {} mobile number: {}",
                loginUser.getUserId(), loginUser.getMobileNumber());

        final var queryCommand = QueryCommand.builder()
                .startTime(startTime)
                .endTime(endTime)
                .build();
        final var availableRoomList = queryAvailableRoomService.query(queryCommand);

        return ResponseModel.<List<AvailableRoom>>builder()
                .status(ResponseStatus.SUCCESS)
                .data(availableRoomList)
                .build();
    }



    @Operation(summary = "Place new booking")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "booking success",
                    content = { @Content(mediaType = "application/json") })})
    @Parameters({
            @Parameter(name = "X-USER-ID", description = "user id",
                    in = ParameterIn.HEADER, schema = @Schema(type = "string")),
            @Parameter(name = "X-USER-MOBILE", description = "user mobile number",
                    in = ParameterIn.HEADER, schema = @Schema(type = "string"))
    })
    @PostMapping("/booking")
    public ResponseModel<BookingDetails> createBooking(@Valid @RequestBody BookingRequest request){
        final var loginUser = httpHelper.getLoginUserInfo();
        log.info("booking request come from user: {} mobile number: {}",
                loginUser.getUserId(), loginUser.getMobileNumber());

        final var bookingCommand = BookingCommand.builder()
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
