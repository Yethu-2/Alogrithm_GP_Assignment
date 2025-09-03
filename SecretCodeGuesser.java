public class SecretCodeGuesser {

    
    public void start() {
        start(new SecretCode()); 
    }

    public void start(SecretCode code) {
        // 1) find correct length
        int correctLength = -1;
        for (int length = 1; length <= 18; length++) {
            String candidate = "B".repeat(length);
            int result = code.guess(candidate);
            if (result != -2) {
                correctLength = length;
                System.out.println("Length: " + correctLength);
                break;
            }
        }
        if (correctLength == -1) {
            System.out.println("Failed to determine secret code length.");
            return;
        }

        // 2) Compute frequencies of each character
        char[] charArray = {'A','B','C','X','I','U'};
        int[] frequency = computeFrequencies(code, correctLength, charArray);

        // Base guess: all 'A'
        String baseGuess = "A".repeat(correctLength);
        int baseGuessCount = frequency[0]; // frequency of 'A'

        // 3) Decide each position
        for (int i = 0; i < baseGuess.length(); i++) {
            char currentChar = baseGuess.charAt(i);
            StringBuilder currentGuess = new StringBuilder(baseGuess);

            // Reorder characters by descending remaining frequency
            bubbleSortByFreqDesc(charArray, frequency);

            for (char c : charArray) {
                if (currentChar == c) continue; // Skip same char
                int ci = getIndex(charArray, c);
                if (frequency[ci] == 0) continue; // Skip exhausted chars

                // test replacement
                currentGuess.setCharAt(i, c);
                int currentGuessCount = code.guess(currentGuess.toString());

                if (currentGuessCount < baseGuessCount) {
                    // original char was correct at i
                    frequency[getIndex(charArray, currentChar)]--;
                    break;
                } else if (currentGuessCount > baseGuessCount) {
                    // new char c is correct at i
                    baseGuess = currentGuess.toString();
                    baseGuessCount = currentGuessCount;
                    frequency[ci]--;
                    break;
                }
            }

            // Terminate early if secret code is found
            if (baseGuessCount == correctLength) {
                break;
            }

            // Optimization: if exactly one char type remains, fill the rest
            int remainingIdx = -1, kinds = 0;
            for (int k = 0; k < frequency.length; k++) {
                if (frequency[k] > 0) { remainingIdx = k; kinds++; }
            }
            // If exactly one char is left, fill it in all remaining positions
            if (kinds == 1) {
                char last = charArray[remainingIdx];
                StringBuilder fill = new StringBuilder(baseGuess);
                for (int p = i + 1; p < fill.length(); p++) {
                    if (fill.charAt(p) != last) fill.setCharAt(p, last);
                }
                baseGuess = fill.toString();
                code.guess(baseGuess);
                break;
            }
        }

        System.out.println("The guess is: " + baseGuess);
    }

    // === Helpers ===

    // compute all frequencies in one loop
    private int[] computeFrequencies(SecretCode code, int length, char[] chars) {
        int[] freq = new int[chars.length];
        for (int i = 0; i < chars.length; i++) {
            freq[i] = code.guess(String.valueOf(chars[i]).repeat(length));
        }
        return freq;
    }

    // get index of char in a char[]
    public int getIndex(char[] array, char c) {
        for (int i = 0; i < array.length; i++) if (array[i] == c) return i;
        return -1;
    }

    // Bubble sort with early termination 
    public void bubbleSortByFreqDesc(char[] chars, int[] freq) {
        if (chars.length != freq.length) {
            throw new IllegalArgumentException("chars and freq must have same length");
        }
        int n = freq.length;
        for (int pass = 0; pass < n - 1; pass++) {
            boolean swapped = false;
            for (int j = 0; j < n - 1 - pass; j++) {
                if (freq[j] < freq[j + 1]) {
                    int tf = freq[j]; freq[j] = freq[j + 1]; freq[j + 1] = tf;
                    char tc = chars[j]; chars[j] = chars[j + 1]; chars[j + 1] = tc;
                    swapped = true;
                }
            }
            if (!swapped) break; 
        }
    }
}
