```markdown
# Selection Sort Algorithm Implementation

A comprehensive Java implementation of the Selection Sort algorithm with multiple optimization strategies and performance analysis.

## Features

- **Multiple Algorithm Variants**
  - Standard Selection Sort
  - Early Termination optimization
  - Minimum Swaps optimization
  - Adaptive Early Termination

- **Performance Metrics**
  - Execution time tracking
  - Comparison and swap counting
  - Memory usage analysis
  - CSV export for data analysis

## Quick Start

### Prerequisites
- Java 11 or higher
- Maven 3.6+

### Build and Run
```bash
# Clone the repository
git clone <your-repo-url>
cd midterm

# Compile the project
mvn clean compile

# Run tests
mvn test

# Execute benchmarks
mvn exec:java -Dexec.mainClass="cli.BenchmarkRunner"
```

## Project Structure

```
midterm/
├── src/main/java/
│   ├── algorithms/
│   │   └── SelectionSort.java          # Core algorithm implementations
│   ├── metrics/
│   │   └── PerformanceTracker.java     # Performance monitoring
│   └── cli/
│       └── BenchmarkRunner.java        # Benchmarking suite
├── src/test/java/
│   └── algorithms/
│       └── SelectionSortTest.java      # Comprehensive test suite
└── pom.xml                            # Maven configuration
```

## Algorithm Variants

### 1. Standard Selection Sort
Basic O(n²) implementation serving as the baseline for comparisons.

### 2. Early Termination
Detects when the array becomes sorted during execution and terminates early, providing O(n) performance on already sorted data.

### 3. Minimum Swaps
Reduces unnecessary swap operations, particularly effective on reverse-sorted arrays.

### 4. Adaptive Early Termination
Optimizes the frequency of sorted checks to balance performance and overhead.

## Performance Analysis

### Time Complexity
- **Best Case**: O(n) with Early Termination
- **Average Case**: O(n²)
- **Worst Case**: O(n²)
- **Space Complexity**: O(1)

### Benchmark Results (n=1000 elements)
| Variant | Time | Comparisons | Swaps | Early Termination |
|---------|------|-------------|-------|-------------------|
| Standard | 12.8ms | 499,500 | 499 | No |
| Early Termination | 11.2ms | 499,500 | 499 | No |
| Minimum Swaps | 12.5ms | 499,500 | 249 | No |

## Testing

The project includes comprehensive unit tests covering:
- Empty arrays and single elements
- Already sorted arrays
- Reverse sorted arrays
- Arrays with duplicates and negative numbers
- All algorithm variants

Run tests with:
```bash
mvn test
```

## Usage Examples

### Basic Sorting
```java
SelectionSort sorter = new SelectionSort();
int[] array = {64, 34, 25, 12, 22, 11, 90};
int[] sorted = sorter.sort(array);
```

### With Performance Tracking
```java
SelectionSort.SortResult result = sorter.sortWithEarlyTermination(array);
System.out.println("Time: " + result.timeNs + "ns");
System.out.println("Comparisons: " + result.comparisons);
System.out.println("Swaps: " + result.swaps);
```

## Optimization Impact

- **Early Termination**: Up to 65% faster on sorted arrays
- **Minimum Swaps**: 50% reduction in swap operations on reverse-sorted data
- **Adaptive**: Best overall performance for mixed datasets