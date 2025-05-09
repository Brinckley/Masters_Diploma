import os
import traceback

from fastapi import APIRouter, HTTPException

from app.model import AudioFileDto, CleanedAudioFileDto
from app.cleaner.audio_cleaner import clean_noise
from app.rest.sender import send_file_info
from app.logger.logger import get_logger

logger = get_logger(__name__)

router = APIRouter()

basic_pitch_host = os.getenv("BASIC_PITCH_HOST")
basic_pitch_port = os.getenv("BASIC_PITCH_PORT")

convert_uri_path = "/convert"

@router.post("/convert", response_model=CleanedAudioFileDto)
async def receive_file(audio_dto: AudioFileDto):
    logger.info(f"File entity for uploading is received {audio_dto.fileName} ")

    if audio_dto.instrumentType == "original":
        filename_dto = audio_dto.fileName
        logger.info(f"File with name {filename_dto} will not be cleaned")
    else:
        try:
            filename_dto = clean_noise(filename=audio_dto.fileName, instrument_type=audio_dto.instrumentType)
        except Exception as e:
            logger.error(f"ERROR in audio processing: {e}")
            traceback.print_exc()
            raise HTTPException(status_code=402, detail=f"ERROR in audio processing: {e}")

    if filename_dto is None:
        return {"result" : "cannot process this file"}

    url = f"http://{basic_pitch_host}:{basic_pitch_port}" + convert_uri_path
    cleaned_audio = CleanedAudioFileDto(fileName=filename_dto)
    logger.info(f"Url for sending the data : {url}, Result is {cleaned_audio}")

    result = await send_file_info(url, cleaned_audio)

    logger.info(f"Response from url for sending the data : {url}, Result is {result}")

    result_midi = CleanedAudioFileDto.model_validate(result)
    return result_midi
