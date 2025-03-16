import logging

from fastapi import APIRouter

logger = logging.getLogger(__name__)

router = APIRouter()

@router.get("/healthcheck")
async def healthcheck():
    logger.error("Basic pitch healthcheck endpoint is finally reached")

    return "success"
