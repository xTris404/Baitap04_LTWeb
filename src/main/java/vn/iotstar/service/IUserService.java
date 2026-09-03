package vn.iotstar.service;

import vn.iotstar.entity.User;

public interface IUserService {

    /** Kiểm tra username/password, trả về User nếu đăng nhập đúng, null nếu sai */
    User login(String username, String password);

    User findByUsername(String username);

    User findById(int id);

    /** Đăng ký tài khoản mới, trả về true nếu thành công */
    boolean register(String username, String password, String email, String fullname, String phone);

    /**
     * Cập nhật hồ sơ cá nhân (fullname, phone, avatar) cho user có id tương ứng.
     * Trả về User đã được cập nhật (để lưu lại vào Session), hoặc null nếu
     * không tìm thấy user.
     */
    User updateProfile(int id, String fullName, String phone, String avatar);

    boolean checkExistEmail(String email);

    boolean checkExistUsername(String username);

    boolean checkExistPhone(String phone);
}
