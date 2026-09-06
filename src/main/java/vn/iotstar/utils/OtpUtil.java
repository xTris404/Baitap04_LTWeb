package vn.iotstar.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public final class OtpUtil {

    private static final int OTP_LENGTH = 6;
    private static final int OTP_VALID_MINUTES = 10;

    private OtpUtil() {
    }

    public static String generateOtp() {
        Random random = new Random();
        int number = 100000 + random.nextInt(900000);
        return String.valueOf(number);
    }

    public static Date expiryFromNow() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, OTP_VALID_MINUTES);
        return cal.getTime();
    }

    public static boolean isExpired(Date expiry) {
        if (expiry == null) {
            return true;
        }
        return expiry.before(new Date());
    }

    public static int getValidMinutes() {
        return OTP_VALID_MINUTES;
    }
}
