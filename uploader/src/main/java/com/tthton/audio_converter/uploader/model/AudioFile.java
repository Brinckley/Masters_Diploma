package com.tthton.audio_converter.uploader.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
public class AudioFile {
    private String userId;

    private MultipartFile audioFile;

    @Builder.Default
    private InstrumentType instrumentType = InstrumentType.OTHER;
}
