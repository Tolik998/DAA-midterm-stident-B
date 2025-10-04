package metrics;

import java.util.HashMap;
import java.util.Map;

/**
 * Enhanced performance tracker for sorting algorithms
 */
public class PerformanceTracker {
    private long startTime;
    private long endTime;
    private int comparisons;
    private int arrayAccesses;
    private int swaps;
    private int iterations;
    private final Map<String, Long> customMetrics;

    public PerformanceTracker() {
        this.customMetrics = new HashMap<>();
        reset();
    }

    public void reset() {
        startTime = 0;
        endTime = 0;
        comparisons = 0;
        arrayAccesses = 0;
        swaps = 0;
        iterations = 0;
        customMetrics.clear();
    }

    public void startTimer() {
        startTime = System.nanoTime();
    }

    public void stopTimer() {
        endTime = System.nanoTime();
    }

    public long getElapsedTime() {
        return endTime - startTime;
    }

    public void recordComparison() {
        comparisons++;
    }

    public void recordComparison(int count) {
        comparisons += count;
    }

    public void recordArrayAccess() {
        arrayAccesses++;
    }

    public void recordArrayAccess(int count) {
        arrayAccesses += count;
    }

    public void recordSwap() {
        swaps++;
    }

    public void recordSwap(int count) {
        swaps += count;
    }

    public void recordIteration() {
        iterations++;
    }

    public void recordIteration(int count) {
        iterations += count;
    }

    public void recordCustomMetric(String name, long value) {
        customMetrics.put(name, value);
    }

    // Getters
    public int getComparisons() { return comparisons; }
    public int getArrayAccesses() { return arrayAccesses; }
    public int getSwaps() { return swaps; }
    public int getIterations() { return iterations; }
    public Map<String, Long> getCustomMetrics() { return new HashMap<>(customMetrics); }

    /**
     * Export metrics as CSV string
     */
    public String toCSV() {
        return String.format("%d,%d,%d,%d,%d",
                getElapsedTime(), comparisons, arrayAccesses, swaps, iterations);
    }

    /**
     * Get CSV header
     */
    public static String getCSVHeader() {
        return "time_ns,comparisons,array_accesses,swaps,iterations";
    }

    @Override
    public String toString() {
        return String.format(
                "Performance Metrics:\n" +
                        "  Time: %d ns\n" +
                        "  Comparisons: %d\n" +
                        "  Array Accesses: %d\n" +
                        "  Swaps: %d\n" +
                        "  Iterations: %d\n" +
                        "  Custom Metrics: %s",
                getElapsedTime(), comparisons, arrayAccesses, swaps, iterations, customMetrics
        );
    }
}