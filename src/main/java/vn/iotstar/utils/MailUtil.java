package vn.iotstar.utils;

import java.io.InputStream;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

/**
 * Gửi OTP qua email bằng Jakarta Mail.
 * Cấu hình trong mail.properties (classpath).
 * Nếu chưa cấu hình SMTP (mail.enabled=false hoặc thiếu thông tin),
 * OTP sẽ được in ra console để vẫn test được.
 */
public final class MailUtil {

    private static final Properties MAIL_PROPS = loadProps();

    private MailUtil() {
    }

    private static Properties loadProps() {
        Properties p = new Properties();
        try (InputStream in = MailUtil.class.getClassLoader()
                .getResourceAsStream("mail.properties")) {
            if (in != null) {
                p.load(in);
            }
        } catch (Exception e) {
            System.err.println("[MailUtil] Không đọc được mail.properties: " + e.getMessage());
        }
        return p;
    }

    public static boolean isMailEnabled() {
        return "true".equalsIgnoreCase(MAIL_PROPS.getProperty("mail.enabled", "false"));
    }

    public static void sendOtp(String toEmail, String otp, String purpose) {
        String subject;
        String body;
        if ("activate".equals(purpose)) {
            subject = "Mã OTP kích hoạt tài khoản";
            body = "Xin chào,\n\nMã OTP kích hoạt tài khoản của bạn là: " + otp
                    + "\nMã có hiệu lực trong " + OtpUtil.getValidMinutes() + " phút.\n\nTrân trọng.";
        } else if ("reset".equals(purpose)) {
            subject = "Mã OTP đặt lại mật khẩu";
            body = "Xin chào,\n\nMã OTP đặt lại mật khẩu của bạn là: " + otp
                    + "\nMã có hiệu lực trong " + OtpUtil.getValidMinutes() + " phút.\n\nTrân trọng.";
        } else {
            subject = "Mã OTP";
            body = "Mã OTP của bạn là: " + otp;
        }

        if (!isMailEnabled()) {
            System.out.println("========== OTP (console fallback) ==========");
            System.out.println("To: " + toEmail);
            System.out.println("Purpose: " + purpose);
            System.out.println("OTP: " + otp);
            System.out.println("============================================");
            return;
        }

        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", MAIL_PROPS.getProperty("mail.smtp.host", "smtp.gmail.com"));
            props.put("mail.smtp.port", MAIL_PROPS.getProperty("mail.smtp.port", "587"));
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            final String username = MAIL_PROPS.getProperty("mail.username");
            final String password = MAIL_PROPS.getProperty("mail.password");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("[MailUtil] Đã gửi OTP tới " + toEmail);
        } catch (Exception e) {
            System.err.println("[MailUtil] Lỗi gửi mail: " + e.getMessage());
            // Fallback console để vẫn test được
            System.out.println("========== OTP (mail failed, console) ==========");
            System.out.println("To: " + toEmail);
            System.out.println("OTP: " + otp);
            System.out.println("================================================");
        }
    }
}
