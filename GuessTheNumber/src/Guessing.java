public class Guessing {

    // Your local variables here
    private int low = 0;
    private int high = 1000;
    private int last_guess = 0;

    /**
     * Implement how your algorithm should make a guess here
     */
    public int guess() {
        int mid = low + (high-low) / 2;
        last_guess = mid;
        return mid;
    }

    /**
     * Implement how your algorithm should update its guess here
     */
    public void update(int answer) {
        if (answer == -1) {
            low = last_guess + 1;
        } else {
            high = last_guess - 1;
        }
    }
}
