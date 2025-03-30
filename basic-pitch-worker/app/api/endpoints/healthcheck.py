import logging

from fastapi import APIRouter

logger = logging.getLogger(__name__)

router = APIRouter()

success_msg = "success"

@router.get("/healthcheck")
async def healthcheck():
    logger.info("Basic pitch healthcheck endpoint is reached")

    return success_msg
