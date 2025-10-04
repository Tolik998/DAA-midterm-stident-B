package algorithms;

import metrics.PerformanceTracker;

/**
 * Optimized Selection Sort implementation with early termination
 * and comprehensive performance tracking.
 *
 * Time Complexity: O(n²) in worst/average case, O(n) in best case (already sorted)
 * Space Complexity: O(1) - in-place sorting
 */
public class SelectionSort {

    /**
     * Sorting result with detailed metrics
     */
    public static class SortResult {
        public final int[] sortedArray;
        public final int comparisons;
        public final int swaps;
        public final int iterations;
        public final long timeNs;
        public final boolean earlyTerminated;

        public SortResult(int[] sortedArray, int comparisons, int swaps,
                          int iterations, long timeNs, boolean earlyTerminated) {
            this.sortedArray = sortedArray;
            this.comparisons = comparisons;
            this.swaps = swaps;
            this.iterations = iterations;
            this.timeNs = timeNs;
            this.earlyTerminated = earlyTerminated;
        }

        @Override
        public String toString() {
            return String.format(
                    "Sorting Result:\n" +
                            "  Elements: %d\n" +
                            "  Comparisons: %d\n" +
                            "  Swaps: %d\n" +
                            "  Iterations: %d\n" +
                            "  Time: %d ns\n" +
                            "  Early Terminated: %s",
                    sortedArray.length, comparisons, swaps, iterations, timeNs, earlyTerminated
            );
        }
    }

    private final PerformanceTracker performanceTracker;

    public SelectionSort() {
        this.performanceTracker = new PerformanceTracker();
    }

    public PerformanceTracker getPerformanceTracker() {
        return performanceTracker;
    }

    /**
     * Standard Selection Sort algorithm
     *
     * @param array the array to be sorted
     * @return sorted array
     * @throws IllegalArgumentException if input array is null
     */
    public int[] sort(int[] array) {
        return sortWithResult(array).sortedArray;
    }

    /**
     * Standard Selection Sort with detailed result metrics
     */
    public SortResult sortWithResult(int[] array) {
        performanceTracker.reset();
        performanceTracker.startTimer();

        validateInput(array);

        int n = array.length;
        int[] workingArray = array.clone();
        boolean earlyTerminated = false;

        performanceTracker.recordArrayAccess(n); // Clone operation

        // Early return for trivial cases
        if (n <= 1) {
            performanceTracker.stopTimer();
            return new SortResult(
                    workingArray,
                    performanceTracker.getComparisons(),
                    performanceTracker.getSwaps(),
                    performanceTracker.getIterations(),
                    performanceTracker.getElapsedTime(),
                    true
            );
        }

        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;

            // Find index of minimum element in remaining unsorted array
            for (int j = i + 1; j < n; j++) {
                performanceTracker.recordComparison();
                performanceTracker.recordArrayAccess(2); // Access workingArray[j] and workingArray[minIndex]

                if (workingArray[j] < workingArray[minIndex]) {
                    minIndex = j;
                }
                performanceTracker.recordIteration();
            }

            // Swap only if necessary
            performanceTracker.recordComparison();
            if (minIndex != i) {
                swap(workingArray, i, minIndex);
                performanceTracker.recordSwap();
            }

            performanceTracker.recordIteration();
        }

        performanceTracker.stopTimer();
        return new SortResult(
                workingArray,
                performanceTracker.getComparisons(),
                performanceTracker.getSwaps(),
                performanceTracker.getIterations(),
                performanceTracker.getElapsedTime(),
                earlyTerminated
        );
    }

    /**
     * Optimized Selection Sort with early termination
     * Terminates early if array becomes sorted during the process
     */
    public SortResult sortWithEarlyTermination(int[] array) {
        performanceTracker.reset();
        performanceTracker.startTimer();

        validateInput(array);

        int n = array.length;
        int[] workingArray = array.clone();
        boolean earlyTerminated = false;
        boolean sorted;

        performanceTracker.recordArrayAccess(n); // Clone operation

        if (n <= 1) {
            performanceTracker.stopTimer();
            return new SortResult(
                    workingArray,
                    performanceTracker.getComparisons(),
                    performanceTracker.getSwaps(),
                    performanceTracker.getIterations(),
                    performanceTracker.getElapsedTime(),
                    true
            );
        }

        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            sorted = true; // Assume array is sorted until proven otherwise

            // Find minimum and check if array is already sorted
            for (int j = i + 1; j < n; j++) {
                performanceTracker.recordComparison();
                performanceTracker.recordArrayAccess(2);

                if (workingArray[j] < workingArray[minIndex]) {
                    minIndex = j;
                    sorted = false;
                } else if (j > i + 1 && workingArray[j] < workingArray[j - 1]) {
                    // Check if elements are in order
                    sorted = false;
                }
                performanceTracker.recordIteration();
            }

            // Early termination if array is already sorted
            if (sorted && minIndex == i) {
                earlyTerminated = true;
                break;
            }

            // Swap if necessary
            performanceTracker.recordComparison();
            if (minIndex != i) {
                swap(workingArray, i, minIndex);
                performanceTracker.recordSwap();
            }

            performanceTracker.recordIteration();
        }

        performanceTracker.stopTimer();
        return new SortResult(
                workingArray,
                performanceTracker.getComparisons(),
                performanceTracker.getSwaps(),
                performanceTracker.getIterations(),
                performanceTracker.getElapsedTime(),
                earlyTerminated
        );
    }

    /**
     * Enhanced Early Termination with adaptive checking
     * Reduces the frequency of sorted checks for better performance
     */
    public SortResult sortWithAdaptiveEarlyTermination(int[] array) {
        performanceTracker.reset();
        performanceTracker.startTimer();

        validateInput(array);

        int n = array.length;
        int[] workingArray = array.clone();
        boolean earlyTerminated = false;

        performanceTracker.recordArrayAccess(n);

        if (n <= 1) {
            performanceTracker.stopTimer();
            return new SortResult(workingArray, 0, 0, 0,
                    performanceTracker.getElapsedTime(), true);
        }

        // Check if already sorted (best case optimization)
        if (isSorted(workingArray)) {
            performanceTracker.stopTimer();
            return new SortResult(workingArray, n-1, 0, 0,
                    performanceTracker.getElapsedTime(), true);
        }

        int earlyTerminationCheckFrequency = Math.max(1, n / 10); // Check every 10% of iterations

        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;

            // Standard selection phase
            for (int j = i + 1; j < n; j++) {
                performanceTracker.recordComparison();
                performanceTracker.recordArrayAccess(2);

                if (workingArray[j] < workingArray[minIndex]) {
                    minIndex = j;
                }
                performanceTracker.recordIteration();
            }

            // Swap if necessary
            performanceTracker.recordComparison();
            if (minIndex != i) {
                swap(workingArray, i, minIndex);
                performanceTracker.recordSwap();
            }

            // Adaptive early termination check
            if (i % earlyTerminationCheckFrequency == 0 && isSortedFromIndex(workingArray, i)) {
                earlyTerminated = true;
                break;
            }

            performanceTracker.recordIteration();
        }

        performanceTracker.stopTimer();
        return new SortResult(
                workingArray,
                performanceTracker.getComparisons(),
                performanceTracker.getSwaps(),
                performanceTracker.getIterations(),
                performanceTracker.getElapsedTime(),
                earlyTerminated
        );
    }

    /**
     * Selection Sort with minimum swaps optimization
     * Reduces unnecessary swaps
     */
    public SortResult sortWithMinimumSwaps(int[] array) {
        performanceTracker.reset();
        performanceTracker.startTimer();

        validateInput(array);

        int n = array.length;
        int[] workingArray = array.clone();

        performanceTracker.recordArrayAccess(n);

        if (n <= 1) {
            performanceTracker.stopTimer();
            return new SortResult(workingArray, 0, 0, 0,
                    performanceTracker.getElapsedTime(), true);
        }

        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            boolean foundSmaller = false;

            // Find minimum and track if any element is smaller than current
            for (int j = i + 1; j < n; j++) {
                performanceTracker.recordComparison();
                performanceTracker.recordArrayAccess(2);

                if (workingArray[j] < workingArray[minIndex]) {
                    minIndex = j;
                    foundSmaller = true;
                }
                performanceTracker.recordIteration();
            }

            // Only swap if we found a smaller element
            if (foundSmaller && minIndex != i) {
                swap(workingArray, i, minIndex);
                performanceTracker.recordSwap();
            }

            performanceTracker.recordIteration();
        }

        performanceTracker.stopTimer();
        return new SortResult(
                workingArray,
                performanceTracker.getComparisons(),
                performanceTracker.getSwaps(),
                performanceTracker.getIterations(),
                performanceTracker.getElapsedTime(),
                false
        );
    }

    // ==================== UTILITY METHODS ====================

    private void validateInput(int[] array) {
        if (array == null) {
            throw new IllegalArgumentException("Input array cannot be null");
        }
    }

    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        performanceTracker.recordArrayAccess(4); // 2 reads + 2 writes
    }

    private boolean isSorted(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            performanceTracker.recordComparison();
            performanceTracker.recordArrayAccess(2);
            if (array[i] > array[i + 1]) {
                return false;
            }
        }
        return true;
    }

    private boolean isSortedFromIndex(int[] array, int fromIndex) {
        for (int i = fromIndex; i < array.length - 1; i++) {
            performanceTracker.recordComparison();
            performanceTracker.recordArrayAccess(2);
            if (array[i] > array[i + 1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the number of inversions in the array
     * Useful for measuring how sorted an array is
     */
    public int countInversions(int[] array) {
        int inversions = 0;
        for (int i = 0; i < array.length; i++) {
            for (int j = i + 1; j < array.length; j++) {
                if (array[i] > array[j]) {
                    inversions++;
                }
            }
        }
        return inversions;
    }
}