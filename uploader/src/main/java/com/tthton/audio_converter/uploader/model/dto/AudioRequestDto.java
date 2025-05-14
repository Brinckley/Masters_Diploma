package com.tthton.audio_converter.uploader.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * Request object for audioFile for conversion
 */
@Data
@Builder
public class AudioRequestDto {
    private static final String USER_ID_NOT_NULL_MESSAGE = "User id must not be null";

    private static final String USER_ID_NOT_EMPTY_MESSAGE = "User id must not be empty";

    private static final String AUDIO_FILE_NOT_NULL_MESSAGE = "Audio file must not be null";

    @NotNull(message = USER_ID_NOT_NULL_MESSAGE)
    @NotEmpty(message = USER_ID_NOT_EMPTY_MESSAGE)
    private String userId;

    @NotNull(message = AUDIO_FILE_NOT_NULL_MESSAGE)
    private MultipartFile audioFile;

    private String instrumentType;
}
