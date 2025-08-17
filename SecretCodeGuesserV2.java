public class SecretCodeGuesserV2 {
    public void check() {
        SecretCode code = new SecretCode();
        int correctLength = -1;

        // 1. Find length
        for (int length = 1; length < 100; length++) {
            String dummy = "A".repeat(length);
            int result = code.guess(dummy);
            if (result != -2) {
                correctLength = length;
                break;
            }
        }
        if (correctLength == -1) {
            System.out.println("Length not found");
            return;
        }
        System.out.println("Correct length is: " + correctLength);

        char[] chars = {'B', 'A', 'C', 'X', 'I', 'U'};
        char[] attempt = new char[correctLength];

        // 2. Determine character frequencies
        java.util.Map<Character, Integer> freq = new java.util.HashMap<>();
        for (char c : chars) {
            String test = String.valueOf(c).repeat(correctLength);
            int count = code.guess(test); // number of c's in secret
            if (count > 0) freq.put(c, count);
        }
        System.out.println("Frequencies: " + freq);

        // 3. Place characters
        java.util.List<Character> pool = new java.util.ArrayList<>();
        for (var e : freq.entrySet()) {
            for (int i = 0; i < e.getValue(); i++) {
                pool.add(e.getKey());
            }
        }

        // start with all 'B' (or dummy char)
        for (int i = 0; i < correctLength; i++) attempt[i] = 'B';
        int correctSoFar = code.guess(new String(attempt));

        // try placing characters from pool
        for (int pos = 0; pos < correctLength; pos++) {
            for (int j = 0; j < pool.size(); j++) {
                char c = pool.get(j);
                char old = attempt[pos];
                attempt[pos] = c;

                int result = code.guess(new String(attempt));
                if (result > correctSoFar) {
                    correctSoFar = result;
                    pool.remove(j); // character placed
                    break;
                } else {
                    attempt[pos] = old; // revert
                }
            }
        }

        System.out.println("Secret code guessed: " + new String(attempt));
    }
}