import os
import logging

from demucs.api import Separator
import torchaudio

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


shared_audio_folder = os.getenv("SHARED_AUDIO_FOLDER")
device = os.getenv("USED_DEVICE")
sample_rate = 44100

def clean_noise(filename: str, instrument_type: str):
    logger.info(f"File received for cleaning: {filename}")

    filepath = os.path.join(shared_audio_folder, filename)
    output_filename = f"cleaned_{filename}"
    output_filepath = os.path.join(shared_audio_folder, output_filename)
    logger.info(f"Expected final path {output_filepath}")

    separator = Separator(model="htdemucs", device=device)

    sources = separator.separate_audio_file(filepath)

    for item in sources:
        if type(item) is dict:
            if instrument_type in item:
                waveform = item[instrument_type]
                torchaudio.save(output_filepath, waveform, sample_rate)
                logger.info(f"Noise-cleaned file saved as {output_filepath}")
    return output_filename