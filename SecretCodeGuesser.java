public class SecretCodeGuesser {
    public void start() {
        Secret code = new Secret(); // Tạo đối tượng Secret (nắm giữ mật mã + hàm guess)
        long startTime = System.currentTimeMillis(); // Ghi lại thời điểm bắt đầu
        int guessCount = 0; // Biến đếm số lần gọi guess()
        StringBuilder secret = new StringBuilder(); // Chuỗi prefix tạm để xây dựng dần mật mã
        char[] possibleChars = {'A', 'B', 'C', 'I', 'U', 'X'}; // Bảng ký tự hợp lệ
        boolean found = true; // Cờ báo xem có tìm được ký tự mới không

        // Vòng lặp chính: tiếp tục chừng nào còn tìm được ký tự mới
        while (found) { 
            found = false; // reset cờ
            for (char c : possibleChars) {
                String guess = secret.toString() + c; // Tạo chuỗi thử: prefix hiện tại + ký tự c
                guessCount++; // tăng số lần đoán
                int feedback = code.guess(guess); // gọi hàm guess() để chấm điểm

                // Nếu feedback == độ dài prefix hiện tại + 1, nghĩa là ký tự vừa thử đúng
                if (feedback == secret.length() + 1) {
                    secret.append(c); // Thêm ký tự này vào prefix
                    found = true; // báo hiệu tìm được ký tự đúng, tiếp tục vòng while
                    break; // dừng vòng for, chuyển sang tìm ký tự tiếp theo
                }
            }
        }

        long timeTaken = System.currentTimeMillis() - startTime; // Tính thời gian chạy
        System.out.println("Number of guesses: " + guessCount);
        System.out.println("I found the secret code. It is " + secret.toString());
        System.out.println("Time taken: " + timeTaken + " ms");
    }
    public static void main(String[] args) {
        new SecretCodeGuesser().start();
    }
}
