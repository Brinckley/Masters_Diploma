import os
import torchaudio
import torch

from demucs.apply import apply_model
from demucs.pretrained import get_model
from demucs.audio import AudioFile

from app.logger.logger import get_logger

logger = get_logger(__name__)

shared_audio_folder = os.getenv("SHARED_AUDIO_FOLDER")
device = torch.device(os.getenv("USED_DEVICE", "cuda" if torch.cuda.is_available() else "cpu"))
sample_rate = 44100


def clean_noise(filename: str, instrument_type: str):
    logger.info(f"File received for cleaning: {filename}")

    filepath = os.path.join(shared_audio_folder, filename)
    output_filename = f"cleaned_{filename}"
    output_filepath = os.path.join(shared_audio_folder, output_filename)

    # Load pretrained Demucs model
    model = get_model('htdemucs').to(device).eval()

    # Load audio using Demucs audio loader
    with AudioFile(filepath) as audio_file:
        audio = audio_file.read(streams=0, samplerate=sample_rate)
        ref = audio.mean(0)  # For stereo
        audio = audio.to(device)

    # Separate the sources
    sources = apply_model(model, audio[None], split=True, overlap=0.25)[0]

    # `model.sources` will contain the names of the instruments
    source_names = model.sources
    stems = dict(zip(source_names, sources))

    if instrument_type not in stems:
        logger.warning(f"Instrument type '{instrument_type}' not found in separated sources.")
        return None

    # Save the requested instrument stem
    waveform = stems[instrument_type].cpu()
    torchaudio.save(output_filepath, waveform, sample_rate)
    logger.info(f"Noise-cleaned file saved as {output_filepath}")

    return output_filename
