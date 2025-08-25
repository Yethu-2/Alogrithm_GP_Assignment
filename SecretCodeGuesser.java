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
    int baseGuessCount = frequencyA;

    // Original version

    // for (int i = 0; i < baseGuess.length(); i++) {
    //     char currentChar = baseGuess.charAt(i);
    //     // String currentGuess = baseGuess;
    //     StringBuilder currentGuess = new StringBuilder(baseGuess); // Version to improve time-complexity

    //     for (char c : charArray) {
    //       if (currentChar == c) {
    //         continue; // No need to check for the same character
    //       }

    //       if (frequency[getIndex(c)] == 0) {
    //         continue; // Character have appeared all frequency already, no need to check
    //       }

    //       // Replace only at index i
    //       // currentGuess = baseGuess.substring(0, i) + c + baseGuess.substring(i + 1);
    //       currentGuess.setCharAt(i, c);
    //       int currentGuessCount = code.guess(currentGuess.toString());

    //       if (currentGuessCount < baseGuessCount) {
    //         frequency[getIndex(currentChar)] = frequency[getIndex(currentChar)] - 1;
    //         break; // Break the loop as we have found the correct character
    //       }

    //       else if (currentGuessCount > baseGuessCount) {
    //         baseGuess = currentGuess.toString(); // Found the correct character at position i, replace baseGuess with new String
    //         baseGuessCount = currentGuessCount;
    //         frequency[getIndex(c)] = frequency[getIndex(c)] - 1;
    //         break;
    //       }
    //     }
    // }

    // System.out.println("The guess is: " + baseGuess);

    
    // // Version with approach to loop through characters based on their frequency
    for (int i = 0; i < baseGuess.length(); i++) {
        char currentChar = baseGuess.charAt(i);
        StringBuilder currentGuess = new StringBuilder(baseGuess);

        // always reorder before testing this position
        reorder(charArray, frequency);

        for (char c : charArray) {
            if (currentChar == c) {
                continue; // skip same char
            }

            if (frequency[getIndex(charArray, c)] == 0) {
                continue; // skip exhausted chars
            }

            // test replacement
            currentGuess.setCharAt(i, c);
            int currentGuessCount = code.guess(currentGuess.toString());

            if (currentGuessCount < baseGuessCount) {
                // original char was correct
                frequency[getIndex(charArray, currentChar)]--;
                break;
            } else if (currentGuessCount > baseGuessCount) {
                // new char is correct
                baseGuess = currentGuess.toString();
                baseGuessCount = currentGuessCount;
                frequency[getIndex(charArray, c)]--;
                break;
            }
        }


        // --- New Optimization: Check if only one character remains ---
        int remainingCharIndex = -1;
        int remainingCount = 0;
        for (int k = 0; k < frequency.length; k++) {
            if (frequency[k] > 0) {
                remainingCharIndex = k;
                remainingCount++;
            }
        }

        // If exactly one char is left, fill it in all remaining positions
        if (remainingCount == 1) {
            char lastChar = charArray[remainingCharIndex];
            StringBuilder fillGuess = new StringBuilder(baseGuess);
            for (int pos = i+1; pos < fillGuess.length(); pos++) {
                if (fillGuess.charAt(pos) != lastChar) {
                    fillGuess.setCharAt(pos, lastChar);
                }
            }
            baseGuess = fillGuess.toString();
            baseGuessCount = code.guess(baseGuess);
            break; // we’re done
        }
    }
    System.out.println("The guess is: " + baseGuess);

}


  // public int getIndex(char c) {
  //   if (c == 'A') {
  //     return 0;
  //   }
  //   else if (c == 'B') {
  //     return 1;
  //   }
  //   else if (c == 'C') {
  //     return 2;
  //   }
  //   else if (c == 'X') {
  //     return 3;
  //   }
  //   else if (c == 'I') {
  //     return 4;
  //   }
  //   else {
  //     return 5;
  //   }
  // }


  // helper: get index of char in charArray
  public int getIndex(char[] charArray, char c) {
    for (int i = 0; i < charArray.length; i++) {
        if (charArray[i] == c) return i;
    }
    return -1;
  }

  // helper: reorder charArray and frequency[] by descending frequency
  public void reorder(char[] arr, int[] freq) {
    for (int i = 0; i < arr.length - 1; i++) {
        for (int j = i + 1; j < arr.length; j++) {
            if (freq[j] > freq[i]) {
                // swap chars
                char tmpC = arr[i];
                arr[i] = arr[j];
                arr[j] = tmpC;
                // swap freqs
                int tmpF = freq[i];
                freq[i] = freq[j];
                freq[j] = tmpF;
            }
        }
    }
  }


}
