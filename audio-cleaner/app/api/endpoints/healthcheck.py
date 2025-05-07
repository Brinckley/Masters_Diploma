import os
import requests

from fastapi import APIRouter

from app.logger.logger import get_logger

logger = get_logger(__name__)

router = APIRouter()

basic_pitch_host = os.getenv("BASIC_PITCH_HOST")
basic_pitch_port = os.getenv("BASIC_PITCH_PORT")

@router.get("/healthcheck")
async def healthcheck():
    logger.info("Healthcheck endpoint in the cleaner service is reached")

    url = "http://" + basic_pitch_host + ":" + basic_pitch_port + "/healthcheck"
    response = requests.get(url)

    return response.json()


@router.get("/healthcheck_solo")
async def healthcheck_solo():
    logger.info("Healthcheck_solo endpoint in the cleaner service is reached")

    return {"status": "ok"}
