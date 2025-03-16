import httpx
import logging
from app.model import FileNameDto

logger = logging.getLogger(__name__)

async def send_post_request(filename: FileNameDto, url: str):
    logger.error(f"Sending POST request to url {url}, Data : {filename.model_dump()}")
    async with httpx.AsyncClient() as client:
        response = await client.post(url, params=filename.model_dump())
        logger.error(f"Received response with code {response.status_code} , data : {response.json()}")
        return response.json()