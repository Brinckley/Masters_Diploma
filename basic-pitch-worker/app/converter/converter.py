import os
from basic_pitch import ICASSP_2022_MODEL_PATH
from basic_pitch.inference import predict_and_save

from app.logger.logger import get_logger

logger = get_logger(__name__)

shared_audio_folder = os.getenv("SHARED_AUDIO_FOLDER")
shared_midi_folder = os.getenv("SHARED_MIDI_FOLDER")

def convert_file(filename: str):
    logger.info(f"File received for conversion: {filename}")
    filepath = os.path.join(shared_audio_folder, filename)

    try:
        if not os.path.exists(filepath):
            logger.error(f"Audio file does not exist: {filepath}")
            raise Exception(f"Audio file does not exist: {filepath}")

        os.makedirs(shared_midi_folder, exist_ok=True)

        predict_and_save([filepath],
                         shared_midi_folder,
                         True,
                         False,
                         False,
                         False,
                         model_or_model_path=ICASSP_2022_MODEL_PATH)

        midi_filename = create_final_midi_name(filename)
        midi_path = os.path.join(shared_midi_folder, midi_filename)

        if not os.path.exists(midi_path):
            logger.error(f"MIDI file was not created: {midi_path}")
            raise Exception(f"MIDI file was not created: {midi_path}")

        os.remove(filepath)

        logger.info(f"MIDI file created successfully: {midi_path}")
        return midi_filename

    except Exception as e:
        logger.error(f"Unexpected error during conversion: {e}")
        raise Exception(f"Unexpected error during conversion: {e}")


def create_final_midi_name(filename):
    base_name, _ = os.path.splitext(filename)
    new_filename = base_name + "_basic_pitch.mid"
    return new_filename
