import os

from generator.midi_generator import generate_midi


midi_output_dir = os.getenv("MIDI_FILES")
gen_num = int(os.getenv("MIDI_NUM"))


def main():
    generate_midi(gen_num, midi_output_dir)
    return 0


if __name__ == '__main__':
    main()

