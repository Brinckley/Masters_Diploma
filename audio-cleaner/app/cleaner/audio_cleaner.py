import os
import shutil
from pathlib import Path
from app.logger.logger import get_logger

logger = get_logger(__name__)

shared_audio_folder = os.getenv("SHARED_AUDIO_FOLDER")
demucs_model = "htdemucs_6s"
device = "cpu"

def clean_noise(filename: str, instrument_type: str) -> str:
    logger.info(f"File received for cleaning: {filename}")

    input_path = os.path.join(shared_audio_folder, filename)
    track_name = Path(filename).stem
    temp_output_dir = os.path.join(shared_audio_folder, "demucs_output")
    source_path = Path(temp_output_dir) / demucs_model / track_name / f"{instrument_type}.wav"

    new_file_name = f"{instrument_type}_{filename}.wav"
    new_source_path = Path(temp_output_dir) / demucs_model / track_name / new_file_name

    os.makedirs(temp_output_dir, exist_ok=True)

    command = f"python3 -m demucs.separate -n {demucs_model} --two-stems {instrument_type} -d {device} \"{input_path}\" -o \"{temp_output_dir}\""
    logger.info(f"Running Demucs command: {command}")
    exit_code = os.system(command)

    if exit_code != 0:
        logger.error("Demucs command failed.")
        raise Exception(f"Demucs command failed exit code {exit_code}")

    if not source_path.exists():
        logger.error(f"Expected cleaned file not found: {source_path}")
        raise Exception(f"Expected cleaned file not found: {source_path}")

    os.rename(source_path, new_source_path)

    shutil.move(str(new_source_path), str(shared_audio_folder))
    logger.info(f"Noise-cleaned file saved as {new_file_name}")

    shutil.rmtree(Path(temp_output_dir) / demucs_model / track_name)
    os.remove(input_path)

    return new_file_name
