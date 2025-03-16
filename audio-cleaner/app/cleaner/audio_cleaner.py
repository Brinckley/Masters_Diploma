import os
import noisereduce as nr
import soundfile as sf
import logging

logger = logging.getLogger(__name__)


shared_audio_folder = os.getenv("SHARED_AUDIO_FOLDER")

def clean_noise(filename):
    logger.error(f"File received for cleaning {filename}")
    filepath = shared_audio_folder + "/" + filename
    logger.error(f"Final file path {filepath}")

    data, rate = sf.read(filepath)
    #reduced_noise = nr.reduce_noise(y=data,
    #                                sr=rate,
    #                                n_std_thresh_stationary=1.5,
    #                                stationary=True)
    logger.error("))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))")