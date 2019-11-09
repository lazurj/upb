package webapp.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;

public class Validators {
    public static int PASSWORD_MIN = 8;
    public static String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
    public static String EMAIL_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";

    public static String password(String password) throws Exception {
        if (password.length() < PASSWORD_MIN) {
            return "<strong>Short password:</strong> Password must be at least 8 characters long.";
        }

        if (!password.matches(PASSWORD_REGEX)) {
            return "<strong>Weak password:</strong> Must contain at least 1 letter and 1 number.";
        }

        if (findPasswordInWeakFile(password)) {
            return "<strong>Weak password:</strong> Common password.";
        }

        return null;
    }

    public static String email(String email) {
        if (!email.matches(EMAIL_REGEX)) {
            return "<strong>Email:</strong> Must be valid email address.";
        }
        return null;
    }

    private static boolean findPasswordInWeakFile(String password) throws Exception {
        URL url = Validators.class.getResource("CommonPasswords.txt");
        File file = new File(url.getPath());

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        while ((st = br.readLine()) != null) {
            if (password.matches(st)) {
                return true;
            }
        }

        return false;
    }

}
