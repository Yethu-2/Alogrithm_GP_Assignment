public class SecretCodeGuesserV2 {
  public void start() {
    SecretCode code = new SecretCode();
    int correctLength = -1;

    // Frequency array for A, B, C, X, I, U
    int[] frequency = new int[6];
    char[] alphabet = {'A', 'B', 'C', 'X', 'I', 'U'};

    // Step 1: Find length and frequencies
    outer:
    for (int length = 1; length <= 18; length++) {
      for (int k = 0; k < alphabet.length; k++) {
        String guessStr = String.valueOf(alphabet[k]).repeat(length);
        int result = code.guess(guessStr);

        if (result == -2) {
          continue; // wrong length
        } else if (result >= 0) {
          // Found the correct length
          correctLength = length;
          frequency[k] = result;

          // Check the remaining letters at this length
          for (int j = 0; j < alphabet.length; j++) {
            if (j != k) {
              String testStr = String.valueOf(alphabet[j]).repeat(correctLength);
              frequency[j] = code.guess(testStr);
            }
          }

          System.out.println("Length: " + correctLength);
          System.out.println("Frequencies: A=" + frequency[0] + 
                             " B=" + frequency[1] + 
                             " C=" + frequency[2] + 
                             " X=" + frequency[3] + 
                             " I=" + frequency[4] + 
                             " U=" + frequency[5]);
          break outer;
        }
      }
    }

    if (correctLength == -1) {
      System.out.println("Failed to determine secret code length.");
      return;
    }

    // Step 2: Build secret code using position tests
    char[] baseGuess = new char[correctLength];
    for (int i = 0; i < correctLength; i++) {
      baseGuess[i] = 'A'; // start with all A
    }
    int baseScore = code.guess(new String(baseGuess));

    for (int pos = 0; pos < correctLength; pos++) {
      char currentChar = baseGuess[pos];

      for (int k = 0; k < alphabet.length; k++) {
        char testChar = alphabet[k];
        if (testChar == currentChar) continue;
        if (frequency[getIndex(testChar)] == 0) continue;

        // Try replacing
        char[] newGuessArr = baseGuess.clone();
        newGuessArr[pos] = testChar;
        String newGuess = new String(newGuessArr);

        int newScore = code.guess(newGuess);
        if (newScore > baseScore) {
          // Found better match
          baseGuess = newGuessArr;
          baseScore = newScore;
          frequency[getIndex(testChar)]--;
          break; 
        } else if (newScore < baseScore) {
          // Current char was correct, reduce its frequency
          frequency[getIndex(currentChar)]--;
          break;
        }
      }
    }

    System.out.println("Secret code found: " + new String(baseGuess));
  }

  // Map letter → index
  public int getIndex(char c) {
    if (c == 'A') return 0;
    else if (c == 'B') return 1;
    else if (c == 'C') return 2;
    else if (c == 'X') return 3;
    else if (c == 'I') return 4;
    else return 5; // U
  }
}
