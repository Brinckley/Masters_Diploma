import logging
import aiohttp
from app.model import FileNameDto

logger = logging.getLogger(__name__)

async def send_file_info(url: str, file_dto: FileNameDto):
    try:
        async with aiohttp.ClientSession() as session:
            async with session.post(url, json=file_dto.model_dump()) as response:
                response.raise_for_status()
                return await response.json()
    except aiohttp.ClientError as e:
        print(f"Error sending file info: {e}")
        return None
