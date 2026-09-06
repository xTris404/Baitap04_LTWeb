package vn.iotstar.service;

import vn.iotstar.entity.User;

public interface IUserService {

    User login(String username, String password);

    User findByUsername(String username);

    User findById(int id);

    User findByEmail(String email);

    User updateProfile(int id, String fullName, String phone, String avatar);

    /**
     * Đăng ký tài khoản mới (inactive), tạo OTP và gửi email.
     * @return true nếu đăng ký thành công
     */
    boolean register(String username, String password, String email, String fullname, String phone);

    boolean activateAccount(String email, String otp);

    boolean resendActivationOtp(String email);

    boolean forgotPassword(String email);

    boolean resetPassword(String email, String otp, String newPassword);

    boolean checkExistEmail(String email);

    boolean checkExistUsername(String username);

    boolean checkExistPhone(String phone);
}
