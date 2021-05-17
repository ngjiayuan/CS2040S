import java.util.HashMap;
import java.util.Random;

/**
 * This is the main class for your Markov Model.
 *
 * Assume that the text will contain ASCII characters in the range [1,255].
 * ASCII character 0 (the NULL character) will be treated as a non-character.
 *
 * Any such NULL characters in the original text should be ignored.
 */
public class MarkovModel {

	private final int order;
	private HashMap<String, int[]> model;
	// Use this to generate random numbers as needed
	private Random generator = new Random();

	// This is a special symbol to indicate no character
	public static final char NOCHARACTER = (char) 0;

	/**
	 * Constructor for MarkovModel class.
	 *
	 * @param order the number of characters to identify for the Markov Model sequence
	 * @param seed the seed used by the random number generator
	 */
	public MarkovModel(int order, long seed) {
		// Initialize your class here
		this.order = order;
		model = new HashMap<>();
		// Initialize the random number generator
		generator.setSeed(seed);
	}

	/**
	 * Builds the Markov Model based on the specified text string.
	 */
	public void initializeText(String text) {
		// Build the Markov model here
		int count = 0;
		while ((count + order) < text.length()) {
			String key = text.substring(count, count + order);
			if (! model.containsKey(key)) {
				int[] value = new int[256];
				if (text.charAt(count + order) != NOCHARACTER) {
					value[text.charAt(count + order)]++;
				}
				model.put(key, value);
			} else {
				if (text.charAt(count + order) != NOCHARACTER) {
					model.get(key)[text.charAt(count + order)]++;
				}
			}
			count++;
		}
	}

	/**
	 * Returns the number of times the specified kgram appeared in the text.
	 */
	public int getFrequency(String kgram) {
		if (kgram.length() != order) {
			throw new IllegalArgumentException("length of kgram not the same as order");
		} else if (! model.containsKey(kgram)) {
			return 0;
		} else {
			int sum = 0;
			for (int i : model.get(kgram)) {
				sum += i;
			}
			return sum;
		}
	}

	/**
	 * Returns the number of times the character c appears immediately after the specified kgram.
	 */
	public int getFrequency(String kgram, char c) {
		if (kgram.length() != order) {
			throw new IllegalArgumentException("length of kgram not the same as order");
		} else if (! model.containsKey(kgram)) {
			return 0;
		} else {
			return model.get(kgram)[c];
		}
	}

	/**
	 * Generates the next character from the Markov Model.
	 * Return NOCHARACTER if the kgram is not in the table, or if there is no
	 * valid character following the kgram.
	 */
	public char nextCharacter(String kgram) {
		// See the problem set description for details
		// on how to make the random selection.
		if (kgram.length() != order) {
			throw new IllegalArgumentException("length of kgram not the same as order");
		} else if (! model.containsKey(kgram)) {
			return NOCHARACTER;
		}
		int kgramFreq = getFrequency(kgram);
		int randomNumber = generator.nextInt(kgramFreq);
		int sum = 0;
		for (int i = 0; i < 256; i++) {
			if (model.get(kgram)[i] != 0) {
				sum += model.get(kgram)[i];
				if (randomNumber < sum) {
					return (char) i;
				}
			}
		}
		return NOCHARACTER;
	}
}
