import os
import subprocess
import logging

logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(name)s - %(levelname)s - %(message)s",
    handlers=[logging.StreamHandler()]
)

logger = logging.getLogger(__name__)

SOUNDFONT_PATH = "/app/FluidR3_GM.sf2"
MIDI_FOLDER = os.getenv("MIDI_FILES")
WAV_FILES = os.getenv("WAV_FILES")


def convert_all_midis(folder_path, final_path):
    midi_files = [f for f in os.listdir(folder_path) if f.endswith(('.mid', '.midi'))]

    logger.info(f"MIDI to piano conversion started ...")
    logger.info(f"Found {len(midi_files)} MIDI files")

    logger.info(f"Conversion started ...")
    for midi_file in midi_files:
        midi_path = os.path.join(folder_path, midi_file)
        wav_path = os.path.join(final_path, os.path.splitext(midi_file)[0] + ".wav")

        logger.info(f"Converting file {midi_file} → {os.path.basename(wav_path)}")

        try:
            subprocess.run([
                "fluidsynth",
                "-ni",
                SOUNDFONT_PATH,
                midi_path,
                "-F", wav_path,
                "-r", "44100"
            ], check=True)
        except subprocess.CalledProcessError as e:
            logger.error(f"Failed to convert {midi_file}: {e}")

    logger.info("All MIDI files converted")


if __name__ == '__main__':
    convert_all_midis(MIDI_FOLDER, WAV_FILES)
