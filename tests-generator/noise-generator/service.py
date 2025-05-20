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
NOISE_SAMPLE_FILE = "mixkit-people-in-fair-ambience-and-laughter-368.wav"
NOISE_LEVEL = float(os.environ.get("NOISE_LEVEL", 0.00))

def add_realistic_noise(clean_data, noise_data, noise_level):
    if noise_data.ndim < clean_data.ndim:
        noise_data = np.tile(noise_data[:, np.newaxis], (1, clean_data.shape[1]))

    if len(noise_data) < len(clean_data):
        repeats = int(np.ceil(len(clean_data) / len(noise_data)))
        noise_data = np.tile(noise_data, (repeats, 1))[:len(clean_data)]
    else:
        noise_data = noise_data[:len(clean_data)]

    noise_data = noise_data / np.max(np.abs(noise_data))
    noisy_data = clean_data + noise_level * noise_data
    return np.clip(noisy_data, -1.0, 1.0)

def process_wav_files():
    if not all([WAV_FILES, NOISY_FILES, NOISE_SAMPLE_FILE]):
        logger.error("Проверьте переменные окружения: WAV_FILES, NOISY_FILES, NOISE_SAMPLE_FILE")
        return

    wav_files = [f for f in os.listdir(WAV_FILES) if f.lower().endswith(".wav")]

    if not wav_files:
        logger.warning("Нет .wav файлов в папке WAV_FILES")
        return

    logger.info(f"Обнаружено {len(wav_files)} WAV файлов")

    try:
        noise_sample, noise_sr = sf.read(NOISE_SAMPLE_FILE)
        noise_sample = noise_sample * 0.5

    except Exception as e:
        logger.error(f"Не удалось загрузить файл с шумом: {e}")
        return

    for wav_file in wav_files:
        input_path = os.path.join(WAV_FILES, wav_file)
        output_path = os.path.join(NOISY_FILES, wav_file)

        try:
            data, samplerate = sf.read(input_path)
            data = data * 2.0

            if noise_sr != samplerate:
                logger.warning(f"Несовпадение частоты дискретизации в {wav_file} (ожидалось {samplerate}, но шум {noise_sr})")

            noisy_data = add_realistic_noise(data, noise_sample, NOISE_LEVEL)
            sf.write(output_path, noisy_data, samplerate)
            logger.info(f"Обработан файл: {wav_file}")
        except Exception as e:
            logger.error(f"Ошибка при обработке {wav_file}: {e}")

if __name__ == '__main__':
    process_wav_files()
