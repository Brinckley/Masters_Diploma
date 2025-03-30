import logging
import aiohttp
from app.model import CleanedAudioFileDto

logger = logging.getLogger(__name__)

async def send_file_info(url: str, file_dto: CleanedAudioFileDto):
    try:
        async with aiohttp.ClientSession() as session:
            async with session.get(url, json=file_dto.model_dump()) as response:
                response.raise_for_status()
                return await response.json()
    except aiohttp.ClientError as e:
        logger.error(f"Error sending file info: {e}")
        return None
