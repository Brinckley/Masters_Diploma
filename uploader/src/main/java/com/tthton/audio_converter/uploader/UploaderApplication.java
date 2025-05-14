package com.tthton.audio_converter.uploader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for Spring Boot
 */
@Slf4j
@SpringBootApplication
public class UploaderApplication {
	public static void main(String[] args) {
		SpringApplication.run(UploaderApplication.class, args);
		log.info("The application has just started!");
	}
}
