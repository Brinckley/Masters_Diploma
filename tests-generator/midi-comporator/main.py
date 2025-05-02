import os
import pretty_midi
import numpy as np
from collections import namedtuple

ORIGINAL_MIDIS_PATH=os.getenv("ORIGINAL_MIDIS")
CONVERTED_MIDIS_PATH=os.getenv("CONVERTED_MIDIS")

Note = namedtuple("Note", ["pitch", "start"])

MATCH_TOLERANCE = 0.05  # in seconds


def extract_notes(midi_file):
    pm = pretty_midi.PrettyMIDI(midi_file)
    notes = []
    for instrument in pm.instruments:
        if instrument.is_drum:
            continue
        for note in instrument.notes:
            notes.append(Note(note.pitch, round(note.start, 3)))
    return sorted(notes, key=lambda n: (n.start, n.pitch))


def match_notes(notes1, notes2, tolerance):
    matched = 0
    used = set()
    for n1 in notes1:
        for i, n2 in enumerate(notes2):
            if i in used:
                continue
            if n1.pitch == n2.pitch and abs(n1.start - n2.start) <= tolerance:
                matched += 1
                used.add(i)
                break
    return matched


def compare_midi_files(original, converted, tolerance=MATCH_TOLERANCE):
    original_notes = extract_notes(original)
    converted_notes = extract_notes(converted)

    TP = match_notes(original_notes, converted_notes, tolerance)
    FP = len(converted_notes) - TP
    FN = len(original_notes) - TP

    precision = TP / (TP + FP) if (TP + FP) > 0 else 0
    recall = TP / (TP + FN) if (TP + FN) > 0 else 0
    f1 = 2 * precision * recall / (precision + recall) if (precision + recall) > 0 else 0

    return precision, recall, f1


def batch_compare(original_folder, converted_folder):
    total_p, total_r, total_f = 0, 0, 0
    count = 0

    for file in os.listdir(original_folder):
        original_path = os.path.join(original_folder, file)
        converted_path = os.path.join(converted_folder, file)

        p, r, f = compare_midi_files(original_path, converted_path)
        print(f"{file}: Precision={p:.2%}, Recall={r:.2%}, F1={f:.2%}")

        total_p += p
        total_r += r
        total_f += f
        count += 1

    if count > 0:
        print("\n--- Average ---")
        print(f"Precision: {total_p / count:.2%}")
        print(f"Recall:    {total_r / count:.2%}")
        print(f"F1 Score:  {total_f / count:.2%}")
    else:
        print("No valid MIDI pairs found.")


if __name__ == "__main__":
    batch_compare(ORIGINAL_MIDIS_PATH, CONVERTED_MIDIS_PATH)
