import logging
from fastapi import APIRouter
from app.models import AudioFileNameDto, MidiFileDto

from app.converter.converter import convert_file

logger = logging.getLogger(__name__)

router = APIRouter()

@router.post("/convert", response_model=AudioFileNameDto)
async def receive_file(dto: AudioFileNameDto):
    logger.error(f"File entity for uploading is received {dto.fileName} ")

    midi_filename = convert_file(filename=dto.fileName)

    logger.error(f"File is converted. New file name {midi_filename}")

    return MidiFileDto(fileName = midi_filename)
