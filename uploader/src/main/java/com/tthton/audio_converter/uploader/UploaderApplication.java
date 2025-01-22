package com.tthton.audio_converter.uploader;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class UploaderApplication {
	private static final Logger log = LoggerFactory.getLogger(UploaderApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(UploaderApplication.class, args);

		log.debug("The application has just started!");
	}
}
