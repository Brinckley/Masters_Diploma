import logging
from fastapi import APIRouter
from app.models import FileNameDto

from app.converter.converter import convert_file

logger = logging.getLogger(__name__)

router = APIRouter()

@router.post("/receive", response_model=FileNameDto)
async def receive_file(dto: FileNameDto):
    logger.info(f"File entity for uploading is received {dto.filePath} ")

    midi_filepath = convert_file(filepath=dto.filePath)

    logger.info(f"File is converted. New path {midi_filepath}")

    return FileNameDto(filePath=midi_filepath)
