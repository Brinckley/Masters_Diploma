package com.tthton.audio_converter.uploader.model.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * Response object after conversion
 */
@Data
@Builder
public class MidiFileResponseDto {
    private String userId;

    private MultipartFile midiFile;
}
