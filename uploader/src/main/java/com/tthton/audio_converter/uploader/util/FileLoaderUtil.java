package com.tthton.audio_converter.uploader.util;

import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@UtilityClass
public class FileLoaderUtil {
    private static final String FILE_DIRECTORY_NAME = "AudioFilesData";

    /**
     * Saving uploaded file to the temporary storage
     *
     * @param fileName file name
     * @param multipartFile file
     * @throws IOException thrown if unable to save the given file
     */
    public static void UploadFile(String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(FILE_DIRECTORY_NAME);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
    }
}
