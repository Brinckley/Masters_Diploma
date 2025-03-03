package com.tthton.audio_converter.uploader.exception;

import lombok.experimental.StandardException;

import java.io.Serial;

/**
 * Custom exception to be thrown inside the project
 */
@StandardException
public class AudioFileException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 4497269616028193137L;

  /**
   * Standard constructor for creating {@link AudioFileException} with a message
   * @param message message
   */
  public AudioFileException(String message) {
    super(message);
  }

  /**
   * Creating {@link AudioFileException} with a format message
   * @param formatMessage message
   * @param args arguments to be put in the string
   */
  public static AudioFileException format(String formatMessage, Object... args) {
    return new AudioFileException(String.format(formatMessage, args));
  }
}
