package cli;

import algorithms.SelectionSort;
import algorithms.SelectionSort.SortResult;

import java.util.Arrays;
import java.util.Random;

/**
 * Comprehensive benchmark runner for Selection Sort variants
 */
public class BenchmarkRunner {
    private final SelectionSort selectionSort;
    private final Random random;

    public BenchmarkRunner() {
        this.selectionSort = new SelectionSort();
        this.random = new Random(42); // Fixed seed for reproducibility
    }

    public void runComprehensiveBenchmarks() {
        System.out.println("SELECTION SORT BENCHMARK SUITE");
        System.out.println("==============================");

        int[] sizes = {100, 500, 1000, 5000, 10000};
        String[] distributions = {"Random", "Sorted", "Reverse Sorted", "Nearly Sorted"};

        System.out.println("\n" + "=".repeat(100));
        System.out.printf("%-12s | %-15s | %-10s | %-10s | %-8s | %-10s | %-10s | %s%n",
                "Size", "Distribution", "Time(ns)", "Compares", "Swaps", "Iterations", "Early Term", "Algorithm");
        System.out.println("=".repeat(100));

        for (int size : sizes) {
            for (String distribution : distributions) {
                int[] array = generateArray(size, distribution);

                // Test all algorithm variants
                benchmarkVariant(array, "Standard", distribution, size);
                benchmarkVariant(array, "EarlyTerm", distribution, size);
                benchmarkVariant(array, "Adaptive", distribution, size);
                benchmarkVariant(array, "MinSwaps", distribution, size);
            }
        }

        runCorrectnessTests();
        runOptimizationAnalysis();
    }

    private void benchmarkVariant(int[] originalArray, String variant, String distribution, int size) {
        int[] array = originalArray.clone();
        SortResult result;

        switch (variant) {
            case "EarlyTerm":
                result = selectionSort.sortWithEarlyTermination(array);
                break;
            case "Adaptive":
                result = selectionSort.sortWithAdaptiveEarlyTermination(array);
                break;
            case "MinSwaps":
                result = selectionSort.sortWithMinimumSwaps(array);
                break;
            default:
                result = selectionSort.sortWithResult(array);
        }

        System.out.printf("%-12d | %-15s | %-10d | %-10d | %-8d | %-10d | %-10s | %s%n",
                size, distribution, result.timeNs, result.comparisons,
                result.swaps, result.iterations, result.earlyTerminated, variant);
    }

    private int[] generateArray(int size, String type) {
        int[] array = new int[size];

        switch (type) {
            case "Sorted":
                for (int i = 0; i < size; i++) array[i] = i;
                break;

            case "Reverse Sorted":
                for (int i = 0; i < size; i++) array[i] = size - i - 1;
                break;

            case "Nearly Sorted":
                for (int i = 0; i < size; i++) array[i] = i;
                // Introduce 10% inversions
                int inversions = size / 10;
                for (int i = 0; i < inversions; i++) {
                    int idx1 = random.nextInt(size);
                    int idx2 = random.nextInt(size);
                    int temp = array[idx1];
                    array[idx1] = array[idx2];
                    array[idx2] = temp;
                }
                break;

            case "Random":
            default:
                for (int i = 0; i < size; i++) array[i] = random.nextInt(size * 10);
                break;
        }

        return array;
    }

    private void runCorrectnessTests() {
        System.out.println("\nCORRECTNESS TESTS");
        System.out.println("=================");

        int[][] testCases = {
                {},                             // empty
                {1},                            // single element
                {1, 2, 3, 4, 5},               // already sorted
                {5, 4, 3, 2, 1},               // reverse sorted
                {3, 1, 4, 1, 5, 9, 2, 6},     // with duplicates
                {-3, 1, -4, 1, -5, 9, -2, 6}  // with negatives
        };

        String[] descriptions = {
                "Empty array",
                "Single element",
                "Already sorted",
                "Reverse sorted",
                "With duplicates",
                "With negatives"
        };

        for (int i = 0; i < testCases.length; i++) {
            testCorrectness(descriptions[i], testCases[i]);
        }
    }

    private void testCorrectness(String description, int[] array) {
        int[] original = array.clone();
        SortResult result = selectionSort.sortWithEarlyTermination(array);

        boolean passed = isSorted(result.sortedArray) &&
                arraysHaveSameElements(original, result.sortedArray);

        System.out.printf("%-20s: %s (Comparisons: %d, Swaps: %d, Early: %s)%n",
                description, passed ? "PASS" : "FAIL",
                result.comparisons, result.swaps, result.earlyTerminated);

        if (!passed) {
            System.out.println("  Original: " + Arrays.toString(original));
            System.out.println("  Sorted:   " + Arrays.toString(result.sortedArray));
        }
    }

    private void runOptimizationAnalysis() {
        System.out.println("\nOPTIMIZATION IMPACT ANALYSIS");
        System.out.println("============================");

        int size = 1000;
        System.out.printf("Array size: %,d elements%n", size);

        // Test on best-case scenario (already sorted)
        int[] sortedArray = generateArray(size, "Sorted");
        analyzeOptimizations("Best Case (Sorted)", sortedArray);

        // Test on worst-case scenario (reverse sorted)
        int[] reverseArray = generateArray(size, "Reverse Sorted");
        analyzeOptimizations("Worst Case (Reverse)", reverseArray);

        // Test on average-case scenario (random)
        int[] randomArray = generateArray(size, "Random");
        analyzeOptimizations("Average Case (Random)", randomArray);
    }

    private void analyzeOptimizations(String scenario, int[] array) {
        System.out.printf("%n%s:%n", scenario);
        System.out.println("-".repeat(60));

        SortResult standard = selectionSort.sortWithResult(array.clone());
        SortResult earlyTerm = selectionSort.sortWithEarlyTermination(array.clone());
        SortResult adaptive = selectionSort.sortWithAdaptiveEarlyTermination(array.clone());
        SortResult minSwaps = selectionSort.sortWithMinimumSwaps(array.clone());

        printComparison("Standard", standard);
        printComparison("Early Termination", earlyTerm);
        printComparison("Adaptive Early Term", adaptive);
        printComparison("Minimum Swaps", minSwaps);

        // Calculate improvements
        double timeImprovement = ((double)(standard.timeNs - earlyTerm.timeNs) / standard.timeNs) * 100;
        double swapImprovement = ((double)(standard.swaps - minSwaps.swaps) / standard.swaps) * 100;

        System.out.printf("Early Termination Time Improvement: %.1f%%%n", timeImprovement);
        System.out.printf("Minimum Swaps Reduction: %.1f%%%n", swapImprovement);
    }

    private void printComparison(String name, SortResult result) {
        System.out.printf("%-20s: Time=%,8d ns | Comparisons=%,6d | Swaps=%,4d | Early=%s%n",
                name, result.timeNs, result.comparisons, result.swaps, result.earlyTerminated);
    }

    private boolean isSorted(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) {
                return false;
            }
        }
        return true;
    }

    private boolean arraysHaveSameElements(int[] arr1, int[] arr2) {
        if (arr1.length != arr2.length) return false;

        int[] copy1 = arr1.clone();
        int[] copy2 = arr2.clone();
        Arrays.sort(copy1);
        Arrays.sort(copy2);

        return Arrays.equals(copy1, copy2);
    }

    public static void main(String[] args) {
        BenchmarkRunner runner = new BenchmarkRunner();
        runner.runComprehensiveBenchmarks();
    }
}