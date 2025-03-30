from fastapi import APIRouter
from app.models import AudioFileNameDto, MidiFileDto

from app.converter.converter import convert_file
from app.logger.logger import get_logger

logger = get_logger(__name__)

router = APIRouter()

@router.post("/convert", response_model=MidiFileDto)
async def receive_file(dto: AudioFileNameDto):
    logger.info(f"File entity for uploading is received {dto.fileName} ")

    midi_filename = convert_file(filename=dto.fileName)

    logger.info(f"File is converted. New file name {midi_filename}")

    return MidiFileDto(fileName = midi_filename)
