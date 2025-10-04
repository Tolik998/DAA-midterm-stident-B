package algorithms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SelectionSortTest {

    private SelectionSort selectionSort;

    @BeforeEach
    void setUp() {
        selectionSort = new SelectionSort();
    }

    @Test
    @DisplayName("Should sort standard unsorted array")
    void testStandardUnsortedArray() {
        int[] input = {64, 25, 12, 22, 11};
        int[] expected = {11, 12, 22, 25, 64};

        SelectionSort.SortResult result = selectionSort.sortWithResult(input);

        assertArrayEquals(expected, result.sortedArray);
        assertTrue(result.comparisons > 0);
        assertTrue(result.swaps > 0);
        assertFalse(result.earlyTerminated);
    }

    @Test
    @DisplayName("Should handle already sorted array with early termination")
    void testAlreadySortedArray() {
        int[] input = {1, 2, 3, 4, 5};
        int[] expected = {1, 2, 3, 4, 5};

        SelectionSort.SortResult result = selectionSort.sortWithEarlyTermination(input);

        assertArrayEquals(expected, result.sortedArray);
        // In best case with early termination, we might have fewer operations
        assertTrue(result.comparisons >= 4); // Minimum comparisons for n=5
    }

    @Test
    @DisplayName("Should handle reverse sorted array")
    void testReverseSortedArray() {
        int[] input = {5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5};

        SelectionSort.SortResult result = selectionSort.sortWithResult(input);

        assertArrayEquals(expected, result.sortedArray);
        // Reverse sorted should have maximum swaps
        assertEquals(2, result.swaps); // For n=5, floor(n/2) swaps
    }

    @Test
    @DisplayName("Should handle empty array")
    void testEmptyArray() {
        int[] input = {};
        int[] expected = {};

        SelectionSort.SortResult result = selectionSort.sortWithResult(input);

        assertArrayEquals(expected, result.sortedArray);
        assertEquals(0, result.comparisons);
        assertEquals(0, result.swaps);
        assertTrue(result.earlyTerminated);
    }

    @Test
    @DisplayName("Should handle single element array")
    void testSingleElementArray() {
        int[] input = {42};
        int[] expected = {42};

        SelectionSort.SortResult result = selectionSort.sortWithResult(input);

        assertArrayEquals(expected, result.sortedArray);
        assertEquals(0, result.comparisons);
        assertEquals(0, result.swaps);
        assertTrue(result.earlyTerminated);
    }

    @Test
    @DisplayName("Should handle array with duplicates")
    void testArrayWithDuplicates() {
        int[] input = {3, 1, 4, 1, 5, 9, 2, 6};
        int[] expected = {1, 1, 2, 3, 4, 5, 6, 9};

        SelectionSort.SortResult result = selectionSort.sortWithResult(input);

        assertArrayEquals(expected, result.sortedArray);
    }

    @Test
    @DisplayName("Should throw exception for null input")
    void testNullInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            selectionSort.sort(null);
        });
    }

    @ParameterizedTest
    @MethodSource("provideArraysForSorting")
    @DisplayName("Parameterized sorting tests")
    void parameterizedSortingTests(int[] input, int[] expected) {
        SelectionSort.SortResult result = selectionSort.sortWithResult(input);
        assertArrayEquals(expected, result.sortedArray);
    }

    private static Stream<Arguments> provideArraysForSorting() {
        return Stream.of(
                Arguments.of(new int[]{1}, new int[]{1}),
                Arguments.of(new int[]{2, 1}, new int[]{1, 2}),
                Arguments.of(new int[]{5, 2, 4, 6, 1, 3}, new int[]{1, 2, 3, 4, 5, 6}),
                Arguments.of(new int[]{-3, 1, -1, 2, 0}, new int[]{-3, -1, 0, 1, 2}),
                Arguments.of(new int[]{5, 5, 5, 5}, new int[]{5, 5, 5, 5})
        );
    }

    @Test
    @DisplayName("Early termination should work on sorted arrays")
    void testEarlyTerminationOnSortedArray() {
        int[] sortedArray = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        SelectionSort.SortResult result = selectionSort.sortWithEarlyTermination(sortedArray);

        assertTrue(result.earlyTerminated);
        assertArrayEquals(sortedArray, result.sortedArray);
    }

    @Test
    @DisplayName("Minimum swaps should reduce swap operations")
    void testMinimumSwapsOptimization() {
        int[] array = {1, 2, 3, 5, 4}; // Only one element out of place

        SelectionSort.SortResult standardResult = selectionSort.sortWithResult(array.clone());
        SelectionSort.SortResult minSwapsResult = selectionSort.sortWithMinimumSwaps(array.clone());

        // Both should produce correct results
        Arrays.sort(array);
        assertArrayEquals(array, standardResult.sortedArray);
        assertArrayEquals(array, minSwapsResult.sortedArray);

        // Minimum swaps should have same or fewer swaps
        assertTrue(minSwapsResult.swaps <= standardResult.swaps);
    }

    @Test
    @DisplayName("Performance metrics should be recorded correctly")
    void testPerformanceMetricsRecording() {
        int[] array = {5, 4, 3, 2, 1};

        SelectionSort.SortResult result = selectionSort.sortWithResult(array);

        assertTrue(result.timeNs > 0, "Should record positive time");
        assertTrue(result.comparisons > 0, "Should record comparisons");
        assertTrue(result.swaps > 0, "Should record swaps");
        assertTrue(result.iterations > 0, "Should record iterations");
    }

    @Test
    @DisplayName("Count inversions should work correctly")
    void testCountInversions() {
        int[] sorted = {1, 2, 3, 4, 5};
        int[] reverse = {5, 4, 3, 2, 1};
        int[] mixed = {2, 4, 1, 3, 5};

        assertEquals(0, selectionSort.countInversions(sorted));
        assertEquals(10, selectionSort.countInversions(reverse)); // n*(n-1)/2 for n=5
        assertTrue(selectionSort.countInversions(mixed) > 0);
    }

    @Test
    @DisplayName("All variants should produce identical sorted results")
    void testAllVariantsProduceSameResults() {
        int[] original = {64, 34, 25, 12, 22, 11, 90};
        int[] expected = original.clone();
        Arrays.sort(expected);

        SelectionSort.SortResult standard = selectionSort.sortWithResult(original.clone());
        SelectionSort.SortResult earlyTerm = selectionSort.sortWithEarlyTermination(original.clone());
        SelectionSort.SortResult adaptive = selectionSort.sortWithAdaptiveEarlyTermination(original.clone());
        SelectionSort.SortResult minSwaps = selectionSort.sortWithMinimumSwaps(original.clone());

        assertArrayEquals(expected, standard.sortedArray);
        assertArrayEquals(expected, earlyTerm.sortedArray);
        assertArrayEquals(expected, adaptive.sortedArray);
        assertArrayEquals(expected, minSwaps.sortedArray);
    }
}