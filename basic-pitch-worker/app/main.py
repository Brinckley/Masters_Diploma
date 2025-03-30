import logging
from fastapi import FastAPI
from app.api.endpoints import file, healthcheck

logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(name)s - %(levelname)s - %(message)s",
    handlers=[logging.StreamHandler()]
)

logger = logging.getLogger(__name__)

app = FastAPI(title="FastAPI File Echo")

app.include_router(file.router)
app.include_router(healthcheck.router)

@app.get("/")
def root():
    logger.info("The root endpoint is reached")

    return {"message": "FastAPI"}
