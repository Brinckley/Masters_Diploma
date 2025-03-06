from pydantic import BaseModel


class FileNameDto(BaseModel):
    filePath: str
