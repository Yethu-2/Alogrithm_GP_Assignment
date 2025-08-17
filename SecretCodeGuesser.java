public class SecretCodeGuesser {
  public void start() {
    // brute force secret code guessing
    SecretCode code = new SecretCode();
    int correctLength = -1; // track correct key length
    
    // First, find correct length by brute-force
    for (int length = 1; length <= 100; length++) {
      String candidate = "B".repeat(length);
      int result = code.guess(candidate);
      if (result != -2) { // not a "wrong length" response
        correctLength = length;
        System.out.println("Length: " + correctLength);
        break;
      }
    }

    if (correctLength == -1) {
      System.out.println("Failed to determine secret code length.");
      return;
    }

    // brute force key guessing
    // String str = "B".repeat(correctLength); // use discovered length
    // while (code.guess(str) != correctLength) {
    //   str = next(str);
    // }
    // System.out.println("I found the secret code. It is " + str);

    // Find the frequency of occurence of each character
    int[] frequency = new int[6];

    int frequencyA = code.guess("A".repeat(correctLength)); // Number of occurence of A in the code
    int frequencyB = code.guess("B".repeat(correctLength)); // Number of occurence of B in the code
    int frequencyC = code.guess("C".repeat(correctLength)); // Number of occurence of C in the code
    int frequencyX = code.guess("X".repeat(correctLength)); // Number of occurence of X in the code
    int frequencyI = code.guess("I".repeat(correctLength)); // Number of occurence of I in the code
    int frequencyU = code.guess("U".repeat(correctLength)); // Number of occurence of U in the code

    frequency[0] = frequencyA;
    frequency[1] = frequencyB;
    frequency[2] = frequencyC;
    frequency[3] = frequencyX;
    frequency[4] = frequencyI;
    frequency[5] = frequencyU;

    char[] charArray = new char[6];
    charArray[0] = 'A';
    charArray[1] = 'B';
    charArray[2] = 'C';
    charArray[3] = 'X';
    charArray[4] = 'I';
    charArray[5] = 'U';

    String baseGuess = "A".repeat(correctLength);
    int baseGuessCount = code.guess(baseGuess);

    for (int i = 0; i < baseGuess.length(); i++) {
        char currentChar = baseGuess.charAt(i);
        String currentGuess = baseGuess;
        for (char c : charArray) {
          if (currentChar == c) {
            continue; // No need to check for the same character
          }

          if (frequency[getIndex(c)] == 0) {
            continue; // Character have appeared all frequency already, no need to check
          }

          // Replace only at index i
          currentGuess = baseGuess.substring(0, i) + c + baseGuess.substring(i + 1);

          int currentGuessCount = code.guess(currentGuess);
          if (currentGuessCount < baseGuessCount) {
            frequency[getIndex(currentChar)] = frequency[getIndex(currentChar)] - 1;
            break; // Break the loop as we have found the correct character
          }

          else if (currentGuessCount > baseGuessCount) {
            baseGuess = currentGuess; // Found the correct character at position i, replace baseGuess with new String
            baseGuessCount = currentGuessCount;
            frequency[getIndex(c)] = frequency[getIndex(c)] - 1;
            break;
          }
        }
    }

    System.out.println("The guess is: " + baseGuess);
  }

  static int order(char c) {
    if (c == 'B') {
      return 0;
    } else if (c == 'A') {
      return 1;
    } else if (c == 'C') {
      return 2;
    } else if (c == 'X') {
      return 3;
    } else if (c == 'I') {
      return 4;
    } 
    return 5;
  }

  static char charOf(int order) {
    if (order == 0) {
      return 'B';
    } else if (order == 1) {
      return 'A';
    } else if (order == 2) {
      return 'C';
    } else if (order == 3) {
      return 'X';
    } else if (order == 4) {
      return 'I';
    } 
    return 'U';
  }

  // return the next value in 'BACXIU' order, that is
  // B < A < C < X < I < U
  public String next(String current) {
    char[] curr = current.toCharArray();
    for (int i = curr.length - 1; i >=0; i--) {
      if (order(curr[i]) < 5) {
        // increase this one and stop
        curr[i] = charOf(order(curr[i]) + 1);
        break;
      }
      curr[i] = 'B';
    }
    return String.valueOf(curr);
  }  

  public int getIndex(char c) {
    if (c == 'A') {
      return 0;
    }
    else if (c == 'B') {
      return 1;
    }
    else if (c == 'C') {
      return 2;
    }
    else if (c == 'X') {
      return 3;
    }
    else if (c == 'I') {
      return 4;
    }
    else {
      return 5;
    }
  }
}
