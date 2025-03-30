import os
import logging
import asyncio

from fastapi import APIRouter

from app.model import AudioFileDto, CleanedAudioFileDto
from app.cleaner.audio_cleaner import clean_noise
from app.rest.sender import send_file_info

logger = logging.getLogger(__name__)

router = APIRouter()

basic_pitch_host = os.getenv("BASIC_PITCH_HOST")
basic_pitch_port = os.getenv("BASIC_PITCH_PORT")

convert_uri_path = "/convert"

@router.post("/convert", response_model=AudioFileDto)
async def receive_file(audio_dto: AudioFileDto):
    logger.error(f"File entity for uploading is received {audio_dto.fileName} ")

    filename_dto = clean_noise(filename=audio_dto.fileName, instrument_type=audio_dto.instrumentType) # TODO fix the cleaning

    url = f"http://{basic_pitch_host}:{basic_pitch_port}" + convert_uri_path
    cleaned_audio = CleanedAudioFileDto(fileName=filename_dto)

    result = await send_file_info(url, cleaned_audio)
    logger.error(f"Url for sending the data : {url}, Result is {result}")

    return result
