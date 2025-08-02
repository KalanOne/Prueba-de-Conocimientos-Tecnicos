import java.util.*;

/**
 * The {@code MinimumOperationsDistinct} class provides a method to determine
 * the minimum number of operations
 * required to make all elements in an integer array distinct by processing the
 * array in segments.
 *
 * <p>
 * The main method, {@link #minOperations(int[])}, iterates through the array
 * and, in each operation,
 * checks for duplicates in the current segment starting from a given index. If
 * duplicates are found,
 * it increments the operation count and moves the starting index forward by
 * three positions.
 * The process repeats until no duplicates are found in the remaining segment.
 * </p>
 *
 * <p>
 * Example usage:
 * 
 * <pre>
 * int[] nums = { 1, 2, 3, 4, 2, 3, 3, 5, 7 };
 * int result = MinimumOperationsDistinct.minOperations(nums); // result is 2
 * </pre>
 * </p>
 *
 * <p>
 * The {@code main} method demonstrates usage with sample arrays.
 * </p>
 */
public class MinimumOperationsDistinct {

    /**
     * Calculates the minimum number of operations required to make all elements in
     * the given array distinct.
     * In each operation, a group of up to 3 consecutive elements can be processed
     * to remove duplicates.
     *
     * @param nums the input array of integers
     * @return the minimum number of operations needed to make all elements distinct
     */
    public static int minOperations(int[] nums) {
        if (nums == null || nums.length <= 1) {
            return 0;
        }

        int operations = 0;
        int n = nums.length;

        int start = 0;

        while (start < n) {
            Set<Integer> seen = new HashSet<>();
            boolean duplicateFound = false;

            for (int i = start; i < n; i++) {
                if (!seen.add(nums[i])) {
                    duplicateFound = true;
                    break;
                }
            }

            if (!duplicateFound) {
                break;
            }

            operations++;
            start += 3;
        }

        return operations;
    }

    /**
     * Demonstrates the usage of the minOperations method with different integer
     * arrays.
     * Each array is passed to minOperations to determine the minimum number of
     * operations
     * required to make all elements in the array distinct.
     * <p>
     * Example outputs:
     * <ul>
     * <li>nums1: {1, 2, 3, 4, 2, 3, 3, 5, 7} -> 2</li>
     * <li>nums2: {4, 5, 6, 4, 4} -> 2</li>
     * <li>nums3: {6, 7, 8, 9} -> 0</li>
     * </ul>
     * The expected output for each test case is printed to the console.
     *
     */
    public static void main(String[] args) {
        int[] nums1 = { 1, 2, 3, 4, 2, 3, 3, 5, 7 };
        int[] nums2 = { 4, 5, 6, 4, 4 };
        int[] nums3 = { 6, 7, 8, 9 };

        System.out.println(minOperations(nums1)); // 2
        System.out.println(minOperations(nums2)); // 2
        System.out.println(minOperations(nums3)); // 0
    }
}
