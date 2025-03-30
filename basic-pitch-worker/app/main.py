from fastapi import FastAPI
from app.api.endpoints import file, healthcheck
from app.logger.logger import get_logger

logger = get_logger(__name__)

app = FastAPI(title="FastAPI File Echo")

app.include_router(file.router)
app.include_router(healthcheck.router)

@app.get("/")
def root():
    logger.info("The root endpoint is reached")

    return {"message": "FastAPI"}
