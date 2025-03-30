from pydantic import BaseModel


class AudioFileNameDto(BaseModel):
    fileName: str

class MidiFileDto(BaseModel):
    fileName: str