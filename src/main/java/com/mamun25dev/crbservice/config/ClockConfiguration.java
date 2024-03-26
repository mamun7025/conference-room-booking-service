package com.mamun25dev.crbservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Slf4j
@Configuration
public class ClockConfiguration {

    @Value("${app.time.zone-id}")
    private String zoneId;

    @Value("${app.time.override-testing-time}")
    private boolean overrideTestingTime;

    @Value("${app.time.override-datetime-value}")
    private String overrideDatetimeValue;

    @Bean
    Clock clock(){
        log.info(">>>>>>>>> zoneId: {}", zoneId);
        log.info(">>>>>>>>> overrideTestingTime: {}", overrideTestingTime);
        if(overrideTestingTime){
            final var localDateTime = LocalDateTime.parse(overrideDatetimeValue, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            Instant instant = localDateTime.atZone(ZoneId.of(zoneId)).toInstant();
            return Clock.fixed(instant, ZoneId.of(zoneId));
        }
        log.info(">>>>>>>>> returning system default clock");
        return Clock.system(ZoneId.of("Asia/Dubai"));
    }
}
