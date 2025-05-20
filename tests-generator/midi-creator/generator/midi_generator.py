import os
import random
import logging

from mido import Message, MidiFile, MidiTrack, bpm2tempo


logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(name)s - %(levelname)s - %(message)s",
    handlers=[logging.StreamHandler()]
)

logger = logging.getLogger(__name__)


SCALE_PATTERNS = {
    "major":     [0, 2, 4, 5, 7, 9, 11],
    "minor":     [0, 2, 3, 5, 7, 8, 10],
    "dorian":    [0, 2, 3, 5, 7, 9, 10],
    "phrygian":  [0, 1, 3, 5, 7, 8, 10],
    "lydian":    [0, 2, 4, 6, 7, 9, 11],
    "mixolydian":[0, 2, 4, 5, 7, 9, 10],
    "locrian":   [0, 1, 3, 5, 6, 8, 10],
}


ROOT_NOTES = {
    "C": 60, "C#": 61, "D": 62, "D#": 63, "E": 64, "F": 65,
    "F#": 66, "G": 67, "G#": 68, "A": 69, "A#": 70, "B": 71
}


def build_scale(root_name, scale_type, octave=4):
    root_midi = ROOT_NOTES[root_name] + (octave - 4) * 12
    intervals = SCALE_PATTERNS[scale_type]
    scale_notes = [root_midi + i for i in intervals]
    return scale_notes


def generate_melody(filepath, scale_notes):
    mid = MidiFile(ticks_per_beat=480)
    track = MidiTrack()
    mid.tracks.append(track)

    total_ticks = 0
    target_seconds = 60
    silence_seconds = 15
    tempo = bpm2tempo(120)  # 120 BPM → 500000 мкс на долю
    ticks_per_second = mid.ticks_per_beat * 1_000_000 / tempo
    target_ticks = int(target_seconds * ticks_per_second)
    silence_ticks = int(silence_seconds * ticks_per_second)

    while total_ticks < target_ticks:
        note = random.choice(scale_notes)
        duration = random.choices(
            [720, 960, 1440, 1920],
            weights=[1, 3, 3, 2],
            k=1
        )[0]
        pause = random.choice([0, 120, 240])

        track.append(Message('note_on', note=note, velocity=64, time=pause))
        track.append(Message('note_off', note=note, velocity=64, time=duration))

        total_ticks += pause + duration

    track.append(Message('note_off', note=0, velocity=0, time=silence_ticks))

    mid.save(filepath)


def generate_midi(num_files: int, output_dir: str):
    logger.info(f"Starting MIDI generation for {num_files} files")

    os.makedirs(output_dir, exist_ok=True)

    for i in range(1, num_files + 1):
        key = random.choice(list(ROOT_NOTES.keys()))
        scale_type = random.choice(list(SCALE_PATTERNS.keys()))
        scale = build_scale(key, scale_type, octave=random.choice([3, 4, 5]))

        filename = f"midi_test_{i}_{key}_{scale_type}.mid"
        filepath = os.path.join(output_dir, filename)
        generate_melody(filepath, scale)

        logger.info(f"Generated MIDI file : {filename}")
