import java.security.SecureRandom;

public class SecretCode {
  private String correctCode;
  private long counter;

  // Allowed alphabet and RNG for generating the secret
  // private static final char[] ALPHABET = {'B','A','C','X','I','U'};
  // private static final SecureRandom RNG = new SecureRandom();

  // private static String generateSecretCode(int length) {
  //   StringBuilder sb = new StringBuilder(length);
  //   for (int i = 0; i < length; i++) {
  //     sb.append(ALPHABET[RNG.nextInt(ALPHABET.length)]);
  //   }
  //   return sb.toString();
  // }

  public SecretCode() {
    // for the real test, your program will not know this
    correctCode = "BACXIUBACXI";
    counter = 0;
  }
  // Returns 
  // -2 : if length of guessedCode is wrong
  // -1 : if guessedCode contains invalid characters
  // >=0 : number of correct characters in correct positions
  public int guess(String guessedCode) {
    counter++;
    // validation
    for (int i = 0; i < guessedCode.length(); i++) {
      char c = guessedCode.charAt(i);
      if (c != 'B' && c != 'A' && c != 'C' && c != 'X' && c != 'I' && c != 'U') {  
       return -1;
      }
    }

    if (guessedCode.length() != correctCode.length()) {
      return -2;
    }
    
    int matched = 0;
    for(int i=0; i < correctCode.length(); i++){
      if(guessedCode.charAt(i) == correctCode.charAt(i)){
        matched++;
      }
    }  
    
    if (matched == correctCode.length()) {
      System.out.println("Number of guesses: " + counter);
    }
    return matched;
  }

  public void reset() {
    counter = 0;
  }

  public static void main(String[] args) {
    

    long t1 = System.currentTimeMillis();
    System.out.println("Running SecretCodeGuesser...");
    new SecretCodeGuesser().start();
    long t2 = System.currentTimeMillis();
    System.out.println("Time taken by SecretCodeGuesser: " + (t2 - t1) + " ms\n");

    // long t3 = System.currentTimeMillis();
    // System.out.println("Running SecretCodeGuesser1...");
    // new SecretCodeGuesser1().start();
    // long t4 = System.currentTimeMillis();
    // System.out.println("Time taken by SecretCodeGuesser1: " + (t4 - t3) + " ms\n");

    long t5 = System.currentTimeMillis();
    System.out.println("Running SecretCodeGuesserV2...");
    new SecretCodeGuesserV2().start();
    long t6 = System.currentTimeMillis();
    System.out.println("Time taken by SecretCodeGuesserV2: " + (t6 - t5) + " ms\n");
  }

  
}
