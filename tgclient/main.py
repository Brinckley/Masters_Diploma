import os

import telebot
from flask import Flask, request
import requests
from telebot import types
from io import BytesIO

app = Flask(__name__)

TOKEN = os.getenv("TOKEN")
bot = telebot.TeleBot(TOKEN)
CONVERTER_URL = os.getenv("CONVERTER_URL")
WEBHOOK_URL = os.getenv("WEBHOOK_URL")

class InMemoryRepository:
    def __init__(self):
        self._store = {}

    def add(self, key, value):
        self._store[key] = value

    def get(self, key):
        return self._store.get(key)

    def remove(self, key):
        if key in self._store:
            del self._store[key]

    def list_all(self):
        return list(self._store.values())


INSTRUMENTS = ["original", "vocals", "drums", "bass", "piano", "guitar", "other"]
repo = InMemoryRepository()


@bot.message_handler(commands=['start', 'help'])
def handle_start(message):
    markup = types.ReplyKeyboardMarkup(resize_keyboard=True)
    btn_original = types.KeyboardButton("original")
    btn_vocals = types.KeyboardButton("vocals")
    btn_drums = types.KeyboardButton("drums")
    btn_bass = types.KeyboardButton("bass")
    btn_piano = types.KeyboardButton("piano")
    btn_guitar = types.KeyboardButton("guitar")
    btn_other = types.KeyboardButton("other")
    markup.row(btn_original, btn_piano)
    markup.row(btn_vocals, btn_drums)
    markup.row(btn_bass, btn_guitar)
    markup.add(btn_other)
    bot.reply_to(message, "Welcome to the audio2midi converter bot!")
    bot.reply_to(message, "Welcome to the audio2midi converter bot!", reply_markup=markup)


@bot.message_handler(content_types=['text'])
def handle_instrument(message):
    if message.text in INSTRUMENTS:
        repo.add(message.chat.id, message.text)
        bot.reply_to(message, f"Instrument {message.text} selected")
    else:
        bot.reply_to(message, f"Unknown message : {message.text}")


def process_audio(instrument, user_id, telegram_audio, server_url=CONVERTER_URL):
    print(telegram_audio)
    file_info = bot.get_file(telegram_audio.file_id)
    print(file_info)
    file = bot.download_file(file_info.file_path)
    mime_type = telegram_audio.mime_type or 'application/octet-stream'
    print(mime_type)

    filename = getattr(telegram_audio, 'file_name', None)
    if not filename:
        ext = {
            'audio/mpeg': '.mp3',
            'audio/wav': '.wav',
            'audio/x-wav': '.wav',
            'audio/ogg': '.ogg',
            'audio/vnd.wave': '.wav'
        }.get(mime_type, '.bin')
        filename = f'input{ext}'

    files = {
        'audioFile': (filename, BytesIO(file), mime_type)
    }
    data = {
        'userId': str(user_id),
        'instrumentType': instrument
    }

    response_health = requests.get(HEALTHCHECK_URL)
    print(f"Sending file {files} with data {data} to {server_url}")
    response = requests.post(server_url, files=files, data=data)
    response.raise_for_status()
    print(f"Response received saving ...")

    if response.status_code == 200:
        midi_io = BytesIO(response.content)
        midi_io.name = 'converted.mid'
        return midi_io
    else:
        print(response.text)
        raise Exception(f"Server error: {response.status_code}")


@bot.message_handler(content_types=['audio', 'document'])
def handle_audio(message):
    instrument = repo.get(message.chat.id)
    if instrument is None:
        bot.reply_to(message, "Select the instrument!")
        return

    if message.content_type == 'audio':
        telegram_audio = message.audio
    elif message.content_type == 'document' and message.document.mime_type.startswith("audio/"):
        telegram_audio = message.document
    else:
        bot.reply_to(message, "Unsupported file type.")
        return

    print(f"Received message with {message}")

    try:
        midi_file = process_audio(instrument, message.chat.id, telegram_audio)
        bot.send_document(message.chat.id, midi_file)
    except Exception as e:
        bot.reply_to(message, f"Error: {str(e)}")


@app.route("/", methods=['POST'])
def webhook():
    json_string = request.get_data(as_text=True)
    update = telebot.types.Update.de_json(json_string)
    bot.process_new_updates([update])
    return "OK", 200


if __name__ == "__main__":
    global data
    bot.remove_webhook()
    bot.set_webhook(url=WEBHOOK_URL)
    app.run()