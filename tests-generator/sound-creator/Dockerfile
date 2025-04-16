FROM python:3.10-slim

RUN apt-get update && \
    apt-get install -y fluidsynth && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY service.py .
COPY FluidR3_GM.sf2 .

CMD ["python", "service.py"]
