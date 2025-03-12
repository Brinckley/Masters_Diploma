import logging
from fastapi import APIRouter

logger = logging.getLogger(__name__)

router = APIRouter()

@router.get("/healthcheck")
async def healthcheck():
    logger.info("Healthcheck endpoint is finally reached")

    return "success"
