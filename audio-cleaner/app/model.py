from pydantic import BaseModel


class AudioFileDto(BaseModel):
    instrumentType: str

    fileName: str

class CleanedAudioFileDto(BaseModel):
    fileName: str
