package com.mamun25dev.crbservice.service.adapter;

import com.mamun25dev.crbservice.dto.command.BookingCommand;
import com.mamun25dev.crbservice.dto.BookingDetails;
import com.mamun25dev.crbservice.service.CreateBookingService;
import org.springframework.stereotype.Service;

@Service
public class CreateBookingServiceImpl implements CreateBookingService {
    @Override
    public BookingDetails create(BookingCommand bookingCommand) {
        return null;
    }
}
