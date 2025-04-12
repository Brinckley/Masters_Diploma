import os




output_dir = os.getenv("OUTPUT_DIR")
gen_num = os.getenv("FILES_NUM")


def main():
    generate_batch(output_dir, gen_num)
    return 0


if __name__ == '__main__':
    main()