import os

from basic_pitch import ICASSP_2022_MODEL_PATH
from basic_pitch.inference import predict_and_save
from pathlib import Path

midi_path = "/midis"

def convert_file(filepath):
    os.makedirs(midi_path, exist_ok=True)
    predict_and_save([filepath], midi_path, True, False, False, False, model_or_model_path=ICASSP_2022_MODEL_PATH)

    file_name = Path(filepath).name
    midi_filepath = midi_path + "/" + file_name
    return midi_filepath