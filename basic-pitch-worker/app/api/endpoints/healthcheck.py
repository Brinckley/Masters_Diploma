from fastapi import APIRouter
from app.logger.logger import get_logger

logger = get_logger(__name__)

router = APIRouter()

success_msg = "success"

@router.get("/healthcheck")
async def healthcheck():
    logger.info("Basic pitch healthcheck endpoint is reached")

    return success_msg
