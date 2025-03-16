import os
import logging
import requests

from fastapi import APIRouter

logger = logging.getLogger(__name__)

router = APIRouter()

basic_pitch_host = os.getenv("BASIC_PITCH_HOST")
basic_pitch_port = os.getenv("BASIC_PITCH_PORT")

@router.get("/healthcheck")
async def healthcheck():
    logger.error("Healthcheck cleaner endpoint is finally reached")

    url = "http://" + basic_pitch_host + ":" + basic_pitch_port + "/healthcheck"
    response = requests.get(url)

    return response.json()
