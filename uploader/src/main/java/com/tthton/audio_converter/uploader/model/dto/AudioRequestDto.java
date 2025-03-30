package com.tthton.audio_converter.uploader.model.dto;

import com.tthton.audio_converter.uploader.model.InstrumentType;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * Request object for audioFile for conversion
 */
@Data
@Builder
public class AudioRequestDto {
    private String userId;

    private MultipartFile audioFile;

    @Builder.Default
    private InstrumentType instrumentType = InstrumentType.OTHER;
}
