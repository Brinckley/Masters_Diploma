import os
import time
import subprocess


SOUNDFONT_PATH = "/app/FluidR3_GM.sf2"
MIDI_FOLDER = os.getenv("MIDI_FILES")


def convert_all_midis(folder_path):
    if not os.path.isdir(folder_path):
        print(f"[ERROR] MIDI folder not found: {folder_path}")
        return

    midi_files = [f for f in os.listdir(folder_path) if f.endswith(('.mid', '.midi'))]

    if not midi_files:
        print("[INFO] No MIDI files to convert.")
        return

    print(f"[INFO] Found {len(midi_files)} MIDI files in {folder_path}. Starting conversion...")

    for midi_file in midi_files:
        midi_path = os.path.join(folder_path, midi_file)
        wav_path = os.path.join(folder_path, os.path.splitext(midi_file)[0] + ".wav")

        print(f"[CONVERTING] {midi_file} → {os.path.basename(wav_path)}")

        try:
            subprocess.run([
                "fluidsynth",
                "-ni",
                SOUNDFONT_PATH,
                midi_path,
                "-F", wav_path,
                "-r", "44100"
            ], check=True)
        except subprocess.CalledProcessError as e:
            print(f"[ERROR] Failed to convert {midi_file}: {e}")

    print("[DONE] All MIDI files converted.")


if __name__ == '__main__':
    print(f"[BOOT] Starting sound-creator with folder: {MIDI_FOLDER}")
    convert_all_midis(MIDI_FOLDER)
