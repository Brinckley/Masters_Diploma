package com.tthton.audio_converter.uploader.controller;

import com.tthton.audio_converter.uploader.rest.AudioFileClient;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest controller for checking service health
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class HealthCheckController {
    private static final String HEALTH_CHECK_RETURN_MSG = "Service is running";

    private static final String GLOBAL_HEALTH_CHECK_RETURN_MSG = "All services are running";

    private static final String GLOBAL_HEALTH_CHECK_FAIL_RETURN_MSG = "Failed to check all of the services";

    private static final String PING_ECHO_MESSAGE = "Hello";

    private static final String PING_ECHO_MESSAGE_RESPONSE = "\"success\"";

    private final AudioFileClient audioFileClient;

    @Observed(name = "healthcheckEndpoint")
    @GetMapping("/healthcheck")
    public String healthCheck() {
        log.info("The health check request arrived");
        return HEALTH_CHECK_RETURN_MSG;
    }

    @Observed(name = "healthcheckGlobalEndpoint")
    @GetMapping("/healthcheck_global")
    public String healthCheckGlobal() {
        log.info("The global health check request arrived to Uploader");

        String echoMsg = audioFileClient.pingEcho(PING_ECHO_MESSAGE);
        log.info("Message received from another service is {}", echoMsg);

        if (echoMsg.equals(PING_ECHO_MESSAGE_RESPONSE)) {
            return GLOBAL_HEALTH_CHECK_RETURN_MSG;
        } else {
            return GLOBAL_HEALTH_CHECK_FAIL_RETURN_MSG;
        }
    }
}
