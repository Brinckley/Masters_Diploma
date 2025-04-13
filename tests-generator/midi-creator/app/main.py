import os

from app.generator.midi.generator import generate_midi

midi_output_dir = "midi_files"
gen_num = 100


def main():
    generate_midi(100, midi_output_dir)
    return 0


if __name__ == '__main__':
    main()

