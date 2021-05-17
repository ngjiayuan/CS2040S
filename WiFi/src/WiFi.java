import java.util.Arrays;

class WiFi {

    /**
     * Implement your solution here
     */
    public static double computeDistance(int[] houses, int numOfAccessPoints) {
        Arrays.sort((houses));
        double begin = 0;
        double end = ((double) houses[houses.length - 1] - (double) houses[0]) / 2;
        while (begin < end) {
            double mid = begin + (end-begin)/2;
            if (coverable(houses, numOfAccessPoints, mid)) {
                end = mid;
            } else {
                begin = mid + 0.5;
            }
        }
        return begin;
    }

    /**
     * Implement your solution here
     */
    public static boolean coverable(int[] houses, int numOfAccessPoints, double distance) {
        Arrays.sort(houses);
        int covered = 0;
        while (numOfAccessPoints > 0) {
            double position = houses[covered] + distance;
            covered++;
            for (int i : houses) {
                if (covered == houses.length) {
                    break;
                } else if (houses[covered] <= position + distance) {
                    covered++;
                }
            }
            numOfAccessPoints--;
        }
        return covered == houses.length;
    }
}
