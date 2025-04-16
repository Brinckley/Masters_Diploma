import os
import logging
import numpy as np
import soundfile as sf


logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(name)s - %(levelname)s - %(message)s",
    handlers=[logging.StreamHandler()]
)

logger = logging.getLogger(__name__)


WAV_FILES = os.environ.get("WAV_FILES")
NOISY_FILES = os.environ.get("NOISY_FILES")
NOISE_LEVEL = float(os.environ.get("NOISE_LEVEL", 0.02))


def add_white_noise(data, noise_level):
    noise = np.random.normal(0, 1, data.shape)
    noise = noise / np.max(np.abs(noise))
    noisy_data = data + noise_level * noise
    return np.clip(noisy_data, -1.0, 1.0)

def process_wav_files():
    wav_files = [f for f in os.listdir(WAV_FILES) if f.endswith(".wav")]

    logger.info(f"Noise generation started ...")
    logger.info(f"Found {len(wav_files)} WAV files")

    for wav_file in wav_files:
        input_path = os.path.join(WAV_FILES, wav_file)
        output_path = os.path.join(NOISY_FILES, wav_file)

        try:
            data, samplerate = sf.read(input_path)
            noisy_data = add_white_noise(data, NOISE_LEVEL)
            sf.write(output_path, noisy_data, samplerate)
            logger.info(f"File converted : {wav_file} → noisy version saved.")
        except Exception as e:
            logger.error(f"Failed to process file {wav_file} : {e}")

if __name__ == '__main__':
    process_wav_files()
