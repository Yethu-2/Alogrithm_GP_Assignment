/**
 * SecretCodeGuesserV3 is a class that attempts to deduce a secret code by interacting
 * with a SecretCode object. The secret code consists of characters from the set {A, B, C, X, I, U}.
 * The guessing process involves three main steps:
 * <ol>
 *   <li><b>Length Discovery:</b> Determines the length of the secret code by submitting guesses of increasing length until a valid response is received.</li>
 *   <li><b>Frequency Discovery:</b> Determines the frequency of each character in the secret code by submitting guesses consisting of repeated single characters.</li>
 *   <li><b>Greedy Placement:</b> Attempts to reconstruct the secret code by greedily placing characters in each position, maximizing the number of correct placements at each step.</li>
 * </ol>
 * 
 * Helper methods are provided to map between character indices and their corresponding characters.
 * 
 * Usage:
 * <pre>
 *     SecretCodeGuesserV3 guesser = new SecretCodeGuesserV3();
 *     guesser.start();
 * </pre>
 * 
 * Note: This class assumes the existence of a SecretCode class with a guess(String) method.
 */
public class SecretCodeGuesserV3 {
    public void start() {
        SecretCode code = new SecretCode();
        int correctLength = -1;

        // Step 1: Find secret code length
        for (int length = 1; length <= 18; length++) {
            String guessStr = "A".repeat(length);
            int result = code.guess(guessStr);
            if (result != -2) {
                correctLength = length;
                System.out.println("Length: " + correctLength);
                break;
            }
        }
        if (correctLength == -1) {
            System.out.println("Failed to determine length.");
            return;
        }

        // Step 2: Frequency discovery
        int[] freq = new int[6]; // index 0=A, 1=B, 2=C, 3=X, 4=I, 5=U
        int totalSoFar = 0;
        for (int i = 0; i < 5; i++) { // only test 5 chars
            char c = getChar(i);
            String guessStr = String.valueOf(c).repeat(correctLength);
            int count = code.guess(guessStr);
            freq[i] = count;
            totalSoFar += count;
        }
        freq[5] = correctLength - totalSoFar; // remaining is U

        System.out.print("Frequencies: ");
        for (int i = 0; i < 6; i++) {
            System.out.print(getChar(i) + "=" + freq[i] + " ");
        }
        System.out.println();

        // Step 3: Placement using greedy approach
        char[] attempt = new char[correctLength];
        for (int i = 0; i < correctLength; i++)
            attempt[i] = 'A';
        int currentScore = code.guess(new String(attempt));

        // Adjust freq for A's already correct in base attempt
        freq[0] -= currentScore;
        if (freq[0] < 0)
            freq[0] = 0;

        for (int pos = 0; pos < correctLength; pos++) {
            char original = attempt[pos];
            for (int i = 0; i < 6; i++) {
                char candidate = getChar(i);
                if (candidate == original)
                    continue;
                if (freq[i] <= 0)
                    continue; // no remaining of this char

                attempt[pos] = candidate;
                int newScore = code.guess(new String(attempt));

                if (newScore > currentScore) {
                    currentScore = newScore;
                    freq[i]--; // use up this char
                    break;
                } else {
                    attempt[pos] = original; // revert
                }
            }

        }

        System.out.println("Secret code found: " + new String(attempt));
    }

    // Helper method to map index to char
    private char getChar(int index) {
        switch (index) {
            case 0:
                return 'A';
            case 1:
                return 'B';
            case 2:
                return 'C';
            case 3:
                return 'X';
            case 4:
                return 'I';
            case 5:
                return 'U';
        }
        return 'A';
    }
}
