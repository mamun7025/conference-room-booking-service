package com.mamun25dev.crbservice.service;

import com.mamun25dev.crbservice.dto.command.BookingCommand;
import com.mamun25dev.crbservice.service.adapter.CreateBookingServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateBookingServiceTest {

    @InjectMocks
    private CreateBookingServiceImpl createBookingService;

    @Test
    public void place_booking(){
        // given
        final var bookingCommand = BookingCommand.builder().build();


        // when
        final var response = createBookingService.create(bookingCommand);


        // then
        Assertions.assertNotNull(response);

    }

}
