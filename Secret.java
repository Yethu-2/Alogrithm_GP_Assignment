
public class Secret { // khai báo lớp 
    private final String secret = "BACXIUBACXIUBA"; // giá trị cố định của mã bí mật private 
 
    public int guess(String guess) {
        int minLength = Math.min(guess.length(), secret.length());
        int correctPositions = 0;
        for (int i = 0; i < minLength; i++) { // so sánh từng ký tự của guess với secret 
            if (guess.charAt(i) == secret.charAt(i)) {
                correctPositions++;
            }
        }
        return correctPositions;
    }
}