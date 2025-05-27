import os
import logging
import numpy as np
import soundfile as sf

# Настройка логирования
logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(name)s - %(levelname)s - %(message)s",
    handlers=[logging.StreamHandler()]
)

logger = logging.getLogger(__name__)

WAV_FILES = os.environ.get("WAV_FILES")
NOISY_FILES = os.environ.get("NOISY_FILES")
NOISE_LEVEL = float(os.environ.get("NOISE_LEVEL", 0.00))

INSTRUMENT_FILES = [
    "mixkit-happy-guitar-chords-2319.wav",
    "mixkit-musical-flute-alert-2308.wav",
    "mixkit-mystwrious-bass-pulse-2298.wav",
   # "mixkit-people-in-fair-ambience-and-laughter-368.wav",
]

INSTRUMENT_VOLUME = 0.05


def load_and_prepare_noise(path, target_shape, channels):
    noise, sr = sf.read(path)

    if noise.ndim < channels:
        noise = np.tile(noise[:, np.newaxis], (1, channels))
    if len(noise) < target_shape[0]:
        repeats = int(np.ceil(target_shape[0] / len(noise)))
        noise = np.tile(noise, (repeats, 1))[:target_shape[0]]
    else:
        noise = noise[:target_shape[0]]

    noise = noise / np.max(np.abs(noise))
    return noise * INSTRUMENT_VOLUME


def process_wav_files():
    if not all([WAV_FILES, NOISY_FILES]):
        return

    wav_files = [f for f in os.listdir(WAV_FILES) if f.lower().endswith(".wav")]
    if not wav_files:
        return

    logger.info(f"Found : {len(wav_files)} WAV files")

    for wav_file in wav_files:
        input_path = os.path.join(WAV_FILES, wav_file)
        output_path = os.path.join(NOISY_FILES, wav_file)

        try:
            data, samplerate = sf.read(input_path)
            if data.ndim == 1:
                data = data[:, np.newaxis]

            combined_data = data.copy() * 2

            for inst_path in INSTRUMENT_FILES:
                try:
                    noise = load_and_prepare_noise(inst_path, data.shape, data.shape[1])
                    combined_data += noise
                except Exception as e:
                    logger.warning(f"Error : {inst_path}: {e}")

            combined_data = np.clip(combined_data, -1.0, 1.0)
            sf.write(output_path, combined_data, samplerate)
            logger.info(f"Current file : {wav_file}")

        except Exception as e:
            logger.error(f"Massive error : {wav_file}: {e}")


if __name__ == '__main__':
    process_wav_files()
