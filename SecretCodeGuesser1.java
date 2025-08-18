public class SecretCodeGuesser1 {
  public void start() {
    SecretCode code = new SecretCode();
    int correctLength = findLength(code);

    if (correctLength == -1) {
      System.out.println("Failed to determine secret code length.");
      return;
    }

    int[] frequency = findFrequencies(code, correctLength);
    String secretCode = findSecretCode(code, correctLength, frequency);

    System.out.println("The guess is: " + secretCode);
  }

  private int findLength(SecretCode code) {
    int low = 1, high = 18, correctLength = -1;

    while (low <= high) {
      int mid = (low + high) / 2;
      String candidate = "B".repeat(mid);
      int result = code.guess(candidate);

      if (result == -2) {
        low = mid + 1;
      } else {
        correctLength = mid;
        high = mid - 1;
      }
    }

    System.out.println("Length: " + correctLength);
    return correctLength;
  }

  private int[] findFrequencies(SecretCode code, int correctLength) {
    int[] frequency = new int[6];
    char[] charArray = {'A', 'B', 'C', 'X', 'I', 'U'};

    for (int i = 0; i < charArray.length; i++) {
      frequency[i] = code.guess(String.valueOf(charArray[i]).repeat(correctLength));
    }

    return frequency;
  }

  private String findSecretCode(SecretCode code, int correctLength, int[] frequency) {
    char[] charArray = {'A', 'B', 'C', 'X', 'I', 'U'};
    StringBuilder baseGuess = new StringBuilder("A".repeat(correctLength));
    int baseGuessCount = code.guess(baseGuess.toString());

    for (int i = 0; i < correctLength; i++) {
      for (char c : charArray) {
        if (frequency[getIndex(c)] == 0) {
          continue;
        }

        char originalChar = baseGuess.charAt(i);
        baseGuess.setCharAt(i, c);
        int currentGuessCount = code.guess(baseGuess.toString());

        if (currentGuessCount > baseGuessCount) {
          baseGuessCount = currentGuessCount;
          frequency[getIndex(c)]--;
          break;
        } else {
          baseGuess.setCharAt(i, originalChar);
        }
      }
    }

    return baseGuess.toString();
  }

  private int getIndex(char c) {
    switch (c) {
      case 'A': return 0;
      case 'B': return 1;
      case 'C': return 2;
      case 'X': return 3;
      case 'I': return 4;
      case 'U': return 5;
      default: return -1;
    }
  }
}
