import os
import logging

from demucs.api import Separator
import torchaudio

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


shared_audio_folder = os.getenv("SHARED_AUDIO_FOLDER")

def clean_noise(filename):
    logger.info(f"File received for cleaning: {filename}")

    filepath = os.path.join(shared_audio_folder, filename)
    output_filename = f"cleaned_{filename}"
    output_filepath = os.path.join(shared_audio_folder, output_filename)
    logger.info(f"Expected final path {output_filepath}")

    separator = Separator(model="htdemucs", device="cpu")
    logger.info("Demucs Separator initialized.")

    sources = separator.separate_audio_file(filepath)
    logger.info(f"The sources type {type(sources)}")

    for item in sources:
        logger.info(f"Type : {type(item)}")
        logger.info(item)
        if type(item) is dict:
            if "other" in item:
                waveform = item["other"]
                sample_rate = 44100  # worth parsing rate
                torchaudio.save(output_filepath, waveform, sample_rate)
                logger.info(f"Noise-cleaned file saved as {output_filepath}")
    return output_filename