FROM python:3.10-slim

WORKDIR /app

RUN apt-get update && \
    apt-get install -y libsndfile1 && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY mixkit-people-in-fair-ambience-and-laughter-368.wav .
COPY service.py .

CMD ["python", "service.py"]
