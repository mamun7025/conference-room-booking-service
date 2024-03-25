package com.mamun25dev.crbservice;

import com.mamun25dev.crbservice.service.SlotCreatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class CrbServiceApplication implements CommandLineRunner {

	private final SlotCreatorService slotCreatorService;

	public static void main(String[] args) {
		SpringApplication.run(CrbServiceApplication.class, args);
	}

	@Override
	public void run(String... args) {
		try {
			log.info("Slot create start..............................................");
			slotCreatorService.createAllConferenceRoomSlots();
			log.info("Slot create end................................................");
		} catch (RuntimeException ex){
			log.error(ex.getMessage());
		}
	}
}
