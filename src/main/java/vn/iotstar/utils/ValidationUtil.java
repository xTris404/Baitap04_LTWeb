package vn.iotstar.utils;

import java.util.regex.Pattern;

public final class ValidationUtil {

    private static final Pattern EMAIL = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE = Pattern.compile("^(0|\\+84)[0-9]{9,10}$");
    private static final Pattern USERNAME = Pattern.compile("^[a-zA-Z0-9_]{3,50}$");
    private static final Pattern PASSWORD = Pattern.compile("^.{6,100}$");
    private static final Pattern OTP = Pattern.compile("^[0-9]{6}$");
    private static final Pattern NUMBER = Pattern.compile("^[0-9]+(\\.[0-9]+)?$");

    private ValidationUtil() {
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL.matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE.matcher(phone.trim()).matches();
    }

    public static boolean isValidUsername(String username) {
        return username != null && USERNAME.matcher(username.trim()).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && PASSWORD.matcher(password).matches();
    }

    public static boolean isValidOtp(String otp) {
        return otp != null && OTP.matcher(otp.trim()).matches();
    }

    public static boolean isValidNumber(String value) {
        return value != null && NUMBER.matcher(value.trim()).matches();
    }

    public static boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
