package vn.iotstar.constants;

public class Constant {

    // Tên thuộc tính lưu trong Session sau khi đăng nhập thành công
    public static final String SESSION_ACCOUNT = "account";

    // Tên Cookie dùng cho chức năng "Nhớ tôi" (Remember me)
    public static final String COOKIE_REMEMBER = "username";

    // Thời gian sống của cookie remember-me: 7 ngày (tính bằng giây)
    public static final int COOKIE_MAX_AGE = 7 * 24 * 60 * 60;

    public static class Path {
        public static final String LOGIN = "/views/login.jsp";
        public static final String REGISTER = "/views/register.jsp";
        public static final String HOME = "/views/home.jsp";
        public static final String PROFILE = "/views/profile.jsp";
    }

    // Thư mục lưu file upload (ảnh danh mục, ...).
    // Dùng user.home thay vì hard-code "E:\\upload" như tài liệu, vì máy chạy
    // thực tế (server) có thể không phải Windows / không có ổ E:.
    // Đổi lại thành đường dẫn cố định nếu bạn muốn, miễn tài khoản chạy Tomcat
    // có quyền ghi vào đó.
    public static final String DIR = System.getProperty("user.home") + java.io.File.separator + "jpa-login-demo-uploads";
}
