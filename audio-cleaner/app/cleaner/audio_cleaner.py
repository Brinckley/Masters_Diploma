import os
import noisereduce as nr
import soundfile as sf
import librosa
import logging

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

shared_audio_folder = os.getenv("SHARED_AUDIO_FOLDER")

def clean_noise(filename):
    logger.info(f"File received for cleaning: {filename}")

    # Construct file path
    filepath = os.path.join(shared_audio_folder, filename)
    logger.info(f"Final file path: {filepath}")

    try:
        # Load audio file
        y, sr = librosa.load(filepath, sr=None)  # Preserve original sample rate
        logger.info("Audio file loaded successfully.")

        # Reduce noise
        y_cleaned = nr.reduce_noise(y=y, sr=sr, prop_decrease=0.8)
        logger.info("Noise reduction applied.")

        # Save cleaned audio file
        cleaned_filename = "cleaned_" + filename
        cleaned_filepath = os.path.join(shared_audio_folder, cleaned_filename)
        sf.write(cleaned_filepath, y_cleaned, sr)
        logger.info(f"Cleaned file saved at: {cleaned_filepath}")

        logger.error("))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))")
        return cleaned_filepath
    except Exception as e:
        logger.error(f"Error processing file: {e}")
        return None