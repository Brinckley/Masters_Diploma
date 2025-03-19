import os
import logging

import torchaudio
import demucs
import demucs.api
from demucs.apply import apply_model
from demucs.pretrained import get_model

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)


shared_audio_folder = os.getenv("SHARED_AUDIO_FOLDER")

def clean_noise(filename):
    logger.info(f"File received for cleaning: {filename}")

    filepath = os.path.join(shared_audio_folder, filename)
    output_filename = f"cleaned_{filename}"
    output_filepath = os.path.join(shared_audio_folder, output_filename)
    logger.info(f"Expected final path {output_filepath}")

    separator = demucs.api.Separator(split=False)
    logger.info(f"Separator created...")

    origin, separated = separator.separate_audio_file(filepath)
    logger.info(f"Separator applied... {origin}")
    logger.info(f"Separator applied... {separated}")

    for item in separated:
        logger.info(f"Item : {item}")
        logger.info(f"Value : {separated[item]}")
        for i in separated[item]:
            logger.error(f"-----------{i}")
        # for file, sources in item:
        #     for stem, source in sources.items():
        #         demucs.api.save_audio(source, f"{stem}_{file}", samplerate=separator.samplerate)

    logger.info(f"Noise-cleaned file saved : {output_filepath}")
    return output_filename