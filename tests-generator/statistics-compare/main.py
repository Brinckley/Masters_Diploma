import re
import matplotlib.pyplot as plt
import numpy as np


def parse_results(file_path):
    results = {}
    with open(file_path, 'r') as f:
        for line in f:
            match = re.match(r"midi_test_(\d+_.*?\.mid)\s+([\d.]+)%\s+([\d.]+)%\s+([\d.]+)%", line)
            if match:
                test_name = match.group(1)
                precision = float(match.group(2))
                recall = float(match.group(3))
                f1 = float(match.group(4))
                results[test_name] = (precision, recall, f1)
    return results


def print_summary_stats(results_dict, noise_lvl, label=""):
    if not results_dict:
        print(f"No results to summarize for {label}")
        return

    precisions = [v[0] for v in results_dict.values()]
    recalls = [v[1] for v in results_dict.values()]
    f1s = [v[2] for v in results_dict.values()]

    print(f"\n--- {label} ---")
    print(f"Total test cases: {len(results_dict)}")
    print(f"Noise level: {noise_lvl}")
    print(f"Average Precision: {np.mean(precisions):.2f}%")
    print(f"Average Recall:    {np.mean(recalls):.2f}%")
    print(f"Average F1 Score:  {np.mean(f1s):.2f}%")
    print("\nDetailed results:")
    for test_name, (prec, rec, f1) in results_dict.items():
        print(f"{test_name}: Precision={prec:.2f}%, Recall={rec:.2f}%, F1={f1:.2f}%")


def plot_top_by_f1(cleaned, not_cleaned, noise_level, top_n=10):
    paired_results = []
    for name in cleaned:
        if name in not_cleaned:
            c_prec, c_rec, c_f1 = cleaned[name]
            n_prec, n_rec, n_f1 = not_cleaned[name]
            delta_f1 = c_f1 - n_f1
            paired_results.append((name, c_f1, n_f1, delta_f1))

    top_25_by_cleaned_f1 = sorted(paired_results, key=lambda x: -x[1])[:25]

    top_final = sorted(top_25_by_cleaned_f1, key=lambda x: -x[3])[:top_n]

    labels = [x[0] for x in top_final]
    c_f1s = [x[1] for x in top_final]
    n_f1s = [x[2] for x in top_final]
    delta_f1s = [x[3] for x in top_final]

    x = np.arange(len(labels))
    width = 0.35

    plt.figure(figsize=(14, 6))

    plt.bar(x - width / 2, n_f1s, width, label='Not Cleaned',
            edgecolor='black', hatch='//', fill=False, linewidth=1.5)
    plt.bar(x + width / 2, c_f1s, width, label='Cleaned',
            edgecolor='black', hatch='\\\\', fill=False, linewidth=1.5)

    for i, delta in enumerate(delta_f1s):
        plt.text(x[i], max(c_f1s[i], n_f1s[i]) + 1,
                 f"ΔF1={delta:.1f}", ha='center', va='bottom', fontsize=8)

    plt.ylabel("F1 Score (%)", fontsize=12)
    plt.title(f"F1 score for complex audio ", fontsize=13)
    plt.xticks(x, labels, rotation=90, fontsize=9)
    plt.yticks(fontsize=10)
    plt.ylim(0, 110)
    plt.grid(axis='y', linestyle='--', linewidth=0.5)
    plt.legend(fontsize=10, loc='upper right')
    plt.tight_layout()
    plt.show()

def plot_recovered_cases(cleaned, not_cleaned, noise_level):
    recovered_cases = []
    for name in cleaned:
        if name in not_cleaned:
            c_f1 = cleaned[name][2]
            n_f1 = not_cleaned[name][2]
            if n_f1 == 0.0 and c_f1 > 0.0:
                recovered_cases.append((name, c_f1))
    if not recovered_cases:
        print(f"No recovered cases for noise level {noise_level}")
        return

    recovered_cases.sort(key=lambda x: -x[1])
    col_labels = ["File Name", "Cleaned F1 (%)", "Not Cleaned F1 (%)"]
    table_data = [
        [name, f"{f1:.2f}", "0.00"] for name, f1 in recovered_cases
    ]

    fig, ax = plt.subplots(figsize=(12, 0.6 * len(table_data) + 1))
    ax.axis('off')

    table = ax.table(
        cellText=table_data,
        colLabels=col_labels,
        loc='center',
        cellLoc='center',
        colLoc='center'
    )
    table.auto_set_font_size(False)
    table.set_fontsize(10)
    table.scale(1, 1.5)

    plt.title(f"Recovered Cases (Noise Level: {noise_level})", fontsize=14, pad=20)
    plt.tight_layout()
    plt.show()

def print_recovered_cases(cleaned, not_cleaned, noise_level):
    recovered_cases = []
    for name in cleaned:
        if name in not_cleaned:
            c_f1 = cleaned[name][2]
            n_f1 = not_cleaned[name][2]
            if n_f1 == 0.0 and c_f1 > 0.0:
                recovered_cases.append((name, c_f1, n_f1))
    if not recovered_cases:
        print(f"No recovered cases for noise level {noise_level}")
        return

    recovered_cases.sort(key=lambda x: -x[1])
    print(f"\n--- Recovered Cases (Noise Level: {noise_level}) ---")
    print(f"{'File Name':<25} {'Cleaned F1 (%)':>15} {'Not Cleaned F1 (%)':>20}")
    for name, c_f1, n_f1 in recovered_cases:
        print(f"{name:<25} {c_f1:15.2f} {n_f1:20.2f}")


if __name__ == "__main__":
    # cleaned_files = ["100_006_midi_results_cleaned.txt", "100_007_midi_results_cleaned.txt",
    #                  "100_008_midi_results_cleaned.txt", "100_009_midi_results_cleaned.txt"]
    # not_cleaned_files = ["100_006_midi_results_not_cleaned.txt", "100_007_midi_results_not_cleaned.txt",
    #                      "100_008_midi_results_not_cleaned.txt", "100_009_midi_results_not_cleaned.txt"]
    # noise_levels = ["0.06", "0.07", "0.08", "0.09"]
    cleaned_files = ["50_multitrack_midi_results_cleaned.txt"]
    not_cleaned_files = ["50_multitrack_midi_results_not_cleaned.txt"]
    noise_levels = ["0"]

    for i in range(len(cleaned_files)):
        cleaned_file = cleaned_files[i]
        not_cleaned_file = not_cleaned_files[i]
        noise_level = noise_levels[i]

        cleaned_results = parse_results(cleaned_file)
        not_cleaned_results = parse_results(not_cleaned_file)

        print_summary_stats(not_cleaned_results, noise_level, label="Not Cleaned")
        print_summary_stats(cleaned_results, noise_level, label="Cleaned")
        plot_recovered_cases(cleaned_results, not_cleaned_results, noise_level)

        plot_top_by_f1(cleaned_results, not_cleaned_results, noise_level, top_n=10)
        print_recovered_cases(cleaned_results, not_cleaned_results, noise_level)
