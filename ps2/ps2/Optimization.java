/**
 * The Optimization class contains a static routine to find the maximum in an array that changes direction at most once.
 */
public class Optimization {

    /**
     * A set of test cases.
     */
    static int[][] testCases = {
            {1, 3, 5, 7, 9, 11, 10, 8, 6, 4},
            {67, 65, 43, 42, 23, 17, 9, 100},
            {4, -100, -80, 15, 20, 25, 30},
            {2, 3, 4, 5, 6, 7, 8, 100, 99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84, 83}
    };

    /**
     * Returns the maximum item in the specified array of integers which changes direction at most once.
     *
     * @param dataArray an array of integers which changes direction at most once.
     * @return the maximum item in data Array
     */
    public static int searchMax(int[] dataArray) {
        // TODO: Implement this
        int len = dataArray.length;
        if (len == 0) { //return -1 for empty array
            System.out.println("Error, array is empty");
            return -1;
        } else if (len == 1) { //return the only element for array of len 1
            return dataArray[0];
        } else {
            // check if it is a maximum array (increases then decreases)
            // or a minimum array (decreases then increases)
            // default is maximum array
            boolean maximum = true;
            // check if array is minimum
            // precondition for this if-statement is every element is unique
            if (dataArray[0] > dataArray[1]) {
                maximum = false;
            }
            // if array is a maximum array, use binary search
            if (maximum) {
                int begin = 0;
                int end = len - 1;
                while (begin < end) {
                    int mid = begin + (end - begin) / 2;
                    if (dataArray[mid + 1] <= dataArray[mid]) { //check if its increasing part or decreasing part
                        end = mid;
                    } else {
                        begin = mid + 1;
                    }
                }
                return dataArray[begin];
            } else { // if array is a minimum array just compare first and last element (works even if array is strictly decreasing)
                if (dataArray[0] > dataArray[len - 1]) {
                    return dataArray[0];
                } else {
                    return dataArray[len - 1];
                }
            }
        }
    }
    /**
     * Runtime for this algo if the input array is a minimum array of size n is constant O(1)
     * Runtime for this algo if the input array is a maximum array of size n is logarithmic O(log n)
     * 1. return -1 and print an error message
     * 2. if the elements are not unique then use linear search with a runtime of O(n)
     * 3. Yes if it changed direction twice the problem is harder,
     *    cannot just compare first and last element for minimum array
     * 4. There is a chance that noisy data will result in a 2 direction change in question 3.
     *    For example, if the array is [2, 3, 4, ...] if entries 3 and 4 are noisy data then,
     *    [2, 4, 3, ...] is possible and we cannot use the same algo as above.
     */

    /**
     * A routine to test the searchMax routine.
     */
    public static void main(String[] args) {
        for (int[] testCase : testCases) {
            System.out.println(searchMax(testCase));
        }
    }
}
