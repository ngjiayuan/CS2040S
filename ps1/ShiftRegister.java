///////////////////////////////////
// This is the main shift register class.
// Notice that it implements the ILFShiftRegister interface.
// You will need to fill in the functionality.
///////////////////////////////////

import java.util.Arrays;

/**
 * class ShiftRegister
 * @ngjiayuan
 * Description: implements the ILFShiftRegister interface.
 */

public class ShiftRegister implements ILFShiftRegister {
    ///////////////////////////////////
    // Create your class variables here
    ///////////////////////////////////
    // TODO:
    static int size;
    static int tap;
    static int[] myseed;


    ///////////////////////////////////
    // Create your constructor here:
    ///////////////////////////////////
    ShiftRegister(int size, int tap) {
        // TODO:
        ShiftRegister.size = size;
        ShiftRegister.tap = tap;
        ShiftRegister.myseed = new int[size];
    }

    ///////////////////////////////////
    // Create your class methods here:
    ///////////////////////////////////
    /**
     * setSeed
     * @param seed
     * Description: use a for loop to check if input seed is valid (only 0s and 1s)
     * if input seed is valid, set myseed as seed
     */
    @Override
    public void setSeed(int[] seed) {
        // TODO:
        boolean valid = true;
        for (int i : seed) {
            if (i != 1 && i != 0) {
                valid = false;
                break;
            }
        }
        if (valid) {
            for (int i = 0; i < size; i++) {
                myseed[i] = seed[i];
            }
        }

    }

    /**
     * shift
     * @return least_significant_bit
     * Description: find value of least sig bit using XOR on most sig bit and tap bit
     * shift the elements in the seed array 1 to the right and
     * replace the least_significant_bit in index 0 of the seed array
     */
    @Override
    public int shift() {
        // TODO:
        int least_significant_bit = myseed[size-1] ^ myseed[tap];
        for (int i = size - 1; i > 0; i--) {
            myseed[i] = myseed[i-1];
        }
        myseed[0] = least_significant_bit;
        return least_significant_bit;
    }

    /**
     * generate
     * @param k
     * @return base-10 integer of the binary int array created by shift operation
     * Description: create a new arr to hold the values returned by the shift operation
     * in order. Subsequently converting the binary int array to base-10 integer using
     * private method toBinary
     */
    @Override
    public int generate(int k) {
        // TODO:
        int[] arr = new int[k];
        int result = 0;
        for (int i = 0; i < k; i++) {
            arr[i] = shift();
        }
        result = toBinary(arr);
        return result;
    }

    /**
     * Returns the integer representation for a binary int array.
     * @param array
     * @return base-10 integer representation for a binary int array
     */
    private int toBinary(int[] array) {
        // TODO:
        int len = array.length;
        int result = 0;
        for (int i = 0; i < len; i++) {
            result += array[i] * Math.pow(2, len - i - 1);
        }
        return result;
    }

}
