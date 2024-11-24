package com.Hackathon.minutesGamification;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.Logger;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MinutesGamificationApplication {
	protected static final Logger logger = LoggerFactory.getLogger(MinutesGamificationApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MinutesGamificationApplication.class, args);
		logger.info("Application is UP");
	}

}
