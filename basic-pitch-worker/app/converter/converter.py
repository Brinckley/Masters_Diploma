import os
import logging

from basic_pitch import ICASSP_2022_MODEL_PATH
from basic_pitch.inference import predict_and_save
from pathlib import Path

logger = logging.getLogger(__name__)
shared_audio_folder = os.getenv("SHARED_AUDIO_FOLDER")
shared_midi_folder = os.getenv("SHARED_MIDI_FOLDER")

def convert_file(filename):
    logger.error(f"File received for conversion {filename}")
    filepath = shared_audio_folder + "/" + filename
    logger.error(f"Final file path {filepath}")

    os.makedirs(shared_midi_folder, exist_ok=True)

    predict_and_save([filepath],
                     shared_midi_folder,
                     True,
                     False,
                     False,
                     False,
                     model_or_model_path=ICASSP_2022_MODEL_PATH)

    file_name = Path(filepath).stem
    midi_filepath = shared_midi_folder + "/" + file_name
    logger.info(f"Midi filepath {midi_filepath}")
    return midi_filepath