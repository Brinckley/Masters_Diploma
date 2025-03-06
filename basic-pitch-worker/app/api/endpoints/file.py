import logging
from fastapi import APIRouter
from app.models import FileNameDto

logger = logging.getLogger(__name__)

router = APIRouter()


@router.post("/echo", response_model=FileNameDto)
async def echo_file(dto: FileNameDto):
    logger.info(f"File entity for uploading is received {dto} ")

    return dto
