import os
import logging
import requests

from fastapi import APIRouter

from app.model import FileNameDto
from app.cleaner.audio_cleaner import clean_noise
from app.rest.sender import send_request


logger = logging.getLogger(__name__)

router = APIRouter()

basic_pitch_host = os.getenv("BASIC_PITCH_HOST")
basic_pitch_port = os.getenv("BASIC_PITCH_PORT")

@router.post("/receive", response_model=FileNameDto)
async def receive_file(dto: FileNameDto):
    logger.error(f"File entity for uploading is received {dto.filePath} ")

    filename_dto = clean_noise(filename=dto.filePath)

    url = "http://" + basic_pitch_host + ":" + basic_pitch_port + "/receive"
    response = send_request(filename_dto, url)

    return response
