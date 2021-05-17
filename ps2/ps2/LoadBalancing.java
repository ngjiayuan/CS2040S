/**
 * Contains static routines for solving the problem of balancing m jobs on p processors
 * with the constraint that each processor can only perform consecutive jobs.
 */
public class LoadBalancing {

    /**
     * Checks if it is possible to assign the specified jobs to the specified number of processors such that no
     * processor's load is higher than the specified query load.
     *
     * @param jobSize the sizes of the jobs to be performed
     * @param queryLoad the maximum load allowed for any processor
     * @param p the number of processors
     * @return true iff it is possible to assign the jobs to p processors so that no processor has more than queryLoad load.
     */
    public static boolean feasibleLoad(int[] jobSize, int queryLoad, int p) {
        // TODO: Implement this
        // idea is to assign load to each processor up to the queryLoad
        int load = 0; // current assigned load to the processor
        int i = 0; // array index
        while (p > 0) {
            if (i == jobSize.length) { // reach end of array
                break;
            } else if (jobSize[i] > queryLoad) { // false if any jobSize is greater than queryLoad
                break;
            } else if (load + jobSize[i] <= queryLoad) { // assigning load up to queryLoad
                load += jobSize[i];
                i++;
            } else { // next processor
                load = 0;
                p--;
            }
        }
        return i == jobSize.length;
    }

    /**
     * Returns the minimum achievable load given the specified jobs and number of processors.
     *
     * @param jobSize the sizes of the jobs to be performed
     * @param p the number of processors
     * @return the maximum load for a job assignment that minimizes the maximum load
     */
    public static int findLoad(int[] jobSize, int p) {
        // TODO: Implement this
        // maximum possible Load is sum of all jobSize
        int sum = 0;
        for (int j : jobSize) {
            sum += j;
        }
        // using binary search and feasibleLoad method to find the load
        // with runtime of O(n log n)
        int begin = sum / p;
        int end = sum;
        while (begin < end) {
            int mid = begin + (end-begin)/2;
            if (feasibleLoad(jobSize, mid, p)) {
                end = mid;
            } else {
                begin = mid + 1;
            }
        }
        return begin;
    }

    // These are some arbitrary testcases.
    public static int[][] testCases = {
            {1, 3, 5, 7, 9, 11, 10, 8, 6, 4},
            {67, 65, 43, 42, 23, 17, 9, 100},
            {4, 100, 80, 15, 20, 25, 30},
            {2, 3, 4, 5, 6, 7, 8, 100, 99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84, 83},
            {7}
    };

    /**
     * Some simple tests for the findLoad routine.
     */
    public static void main(String[] args) {
        for (int p = 1; p < 30; p++) {
            System.out.println("Processors: " + p);
            for (int[] testCase : testCases) {
                System.out.println(findLoad(testCase, p));
            }
        }
    }
}
