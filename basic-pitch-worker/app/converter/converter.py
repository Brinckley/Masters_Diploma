import os

from basic_pitch import ICASSP_2022_MODEL_PATH
from basic_pitch.inference import predict_and_save

shared_audio_folder = os.getenv("SHARED_AUDIO_FOLDER")
shared_midi_folder = os.getenv("SHARED_MIDI_FOLDER")

from app.logger.logger import get_logger

logger = get_logger(__name__)

def convert_file(filename: str):
    logger.info(f"File received for conversion {filename}")
    filepath = shared_audio_folder + "/" + filename

    os.makedirs(shared_midi_folder, exist_ok=True)

    predict_and_save([filepath],
                     shared_midi_folder,
                     True,
                     False,
                     False,
                     False,
                     model_or_model_path=ICASSP_2022_MODEL_PATH)

    midi_filename = change_extension_to_mid(filename)
    logger.info(f"Midi fileName {midi_filename}")
    return midi_filename


def change_extension_to_mid(filename):
    base_name, _ = os.path.splitext(filename)
    new_filename = base_name + ".mid"
    return new_filename