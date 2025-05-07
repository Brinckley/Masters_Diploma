import os
import logging
import requests

logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(name)s - %(levelname)s - %(message)s",
    handlers=[logging.StreamHandler()]
)

logger = logging.getLogger(__name__)

NOISY_DIR = os.getenv("NOISY_FILES")
UPLOADER_CONVERTER_URL = os.getenv("UPLOADER_CONVERTER_URL")
UPLOADER_HEALTHCHECK = os.getenv("UPLOADER_HEALTHCHECK")
MIDI_OUTPUT_DIR = os.getenv("OUTPUT_FILES")
USER_ID = os.getenv("USER_ID", "default-user-id-0")


def check_health(uploader_base_url):
    try:
        response = requests.get(uploader_base_url, timeout=5)
        response.raise_for_status()
        logger.info(f"{uploader_base_url} OK: {response.text}")
    except Exception as e:
        logger.error(f"{uploader_base_url} FAILED: {e}")


def send_files_to_uploader(noisy_files_dir, uploader_url, output_dir, user_id):
    logger.info(f"Start sending test files to url : {uploader_url}")

    for filename in os.listdir(noisy_files_dir):
        if filename.lower().endswith(".wav"):
            filepath = os.path.join(noisy_files_dir, filename)
            logger.info(f"Sending {filename} to uploader...")

            files = {'audioFile': (filename, open(filepath, 'rb'), 'audio/wav')}
            data = {
                'userId': user_id,
                'instrumentType': 'piano',
            }

            try:
                response = requests.get(uploader_url, files=files, data=data)
                response.raise_for_status()
                logger.info(f"Response received saving ...")

                midi_name = filename.replace(".wav", ".mid")
                midi_path = os.path.join(output_dir, midi_name)

                with open(midi_path, 'wb') as out:
                    out.write(response.content)

                logger.info(f"Saved response to {midi_path}")
            except Exception as e:
                logger.error(f"Failed to send {filename}: {e}")


if __name__ == '__main__':
    logger.info(f"Sending healthcheck check to {UPLOADER_HEALTHCHECK}")
    check_health(UPLOADER_HEALTHCHECK)

    logger.info(f"Sending data for conversion to the url {UPLOADER_CONVERTER_URL}")
    send_files_to_uploader(NOISY_DIR, UPLOADER_CONVERTER_URL, MIDI_OUTPUT_DIR, USER_ID)
