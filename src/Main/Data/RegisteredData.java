package Main.Data;

import com.email.durgesh.Email;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import javax.mail.MessagingException;

public class RegisteredData {

    private static String username;
    private static String password;
    private static String email;
    private static int code;

    public void sendCode() throws MessagingException, UnsupportedEncodingException {
        Random random = new Random();
        code = random.nextInt(99999 - 10000) + 10000;

        Email sender = new Email("credlock.test@gmail.com", "Credlock_12345");
        sender.setFrom("credlock.test@gmail.com", "CredLock");
        sender.setSubject("Verification code.");
        sender.setContent("This is your verification code: " + code, "text/html");
        sender.addRecipient(email);
        sender.send();
    }

    public static void setUsername(String username) {
        RegisteredData.username = username;
    }

    public static String getUsername() {
        return username;
    }

    public static void setPassword(String password) {
        RegisteredData.password = password;
    }

    public static String getPassword() {
        return password;
    }

    public static void setEmail(String email) {
        RegisteredData.email = email;
    }

    public static String getEmail() {
        return email;
    }

    public static int getCode() {
        return code;
    }
}
