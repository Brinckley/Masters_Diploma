package com.tthton.audio_converter.uploader.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HealthCheckController {
    private static final String HEALTH_CHECK_RETURN_MSG = "Service is running";

    private static final Logger log = LoggerFactory.getLogger(HealthCheckController.class);

    @GetMapping("/healthcheck")
    public String healthCheck() {
        log.info("The health check request arrived");
        return HEALTH_CHECK_RETURN_MSG;
    }
}
