import httpx
from app.model import FileNameDto

async def send_request(filename: FileNameDto, url: str):
    async with httpx.AsyncClient() as client:
        response = await client.get(url, params=filename.model_dump())
        print(response.status_code, response.json())
        return response.json()