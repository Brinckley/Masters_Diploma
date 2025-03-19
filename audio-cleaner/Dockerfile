FROM python:3.9

WORKDIR /app

RUN apt-get update && apt-get install -y \
    libsndfile1 \
    libsndfile-dev \
    ffmpeg

ENV SNDFILE_INSTALL_DIR=/usr/lib/x86_64-linux-gnu

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

EXPOSE 8001

CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8001"]
