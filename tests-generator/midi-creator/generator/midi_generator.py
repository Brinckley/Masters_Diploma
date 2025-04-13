import os
import random
from mido import Message, MidiFile, MidiTrack


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


def generate_melody(filepath, note_count, scale_notes):
    mid = MidiFile()
    track = MidiTrack()
    mid.tracks.append(track)

    for _ in range(note_count):
        note = random.choice(scale_notes)
        duration = random.choices(
            [720, 960, 1440, 1920],
            weights=[1, 3, 3, 2],
            k=1)[0]
        pause = random.choice([0, 120, 240])  # random delay before next note

        track.append(Message('note_on', note=note, velocity=64, time=pause))
        track.append(Message('note_off', note=note, velocity=64, time=duration))

    mid.save(filepath)



def generate_midi(num_files: int, output_dir: str):
    for i in range(1, num_files + 1):
        key = random.choice(list(ROOT_NOTES.keys()))
        scale_type = random.choice(list(SCALE_PATTERNS.keys()))
        scale = build_scale(key, scale_type, octave=random.choice([3, 4, 5]))

        note_count = random.randint(64, 128)
        filename = f"midi_test_{i}_{key}_{scale_type}.mid"
        generate_melody(os.path.join(output_dir, filename), note_count, scale)
        print(f"Saved: {filename}")
