package com.mamun25dev.crbservice.service;

import com.mamun25dev.crbservice.dto.command.BookingCommand;
import com.mamun25dev.crbservice.dto.BookingDetails;

public interface CreateBookingService {
    BookingDetails create(BookingCommand bookingCommand);
}
