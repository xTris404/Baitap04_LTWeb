package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.constants.Constant;
import vn.iotstar.service.IUserService;
import vn.iotstar.service.UserServiceImpl;
import vn.iotstar.utils.ValidationUtil;

@WebServlet(urlPatterns = { "/register" })
public class RegisterController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final IUserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirm");
        String email = req.getParameter("email");
        String fullname = req.getParameter("fullname");
        String phone = req.getParameter("phone");

        req.setAttribute("username", username);
        req.setAttribute("email", email);
        req.setAttribute("fullname", fullname);
        req.setAttribute("phone", phone);

        if (!ValidationUtil.isValidUsername(username)) {
            req.setAttribute("alert", "Username phải 3-50 ký tự (chữ, số, _).");
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }
        if (!ValidationUtil.isValidPassword(password)) {
            req.setAttribute("alert", "Mật khẩu tối thiểu 6 ký tự.");
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }
        if (confirm == null || !password.equals(confirm)) {
            req.setAttribute("alert", "Mật khẩu xác nhận không khớp.");
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }
        if (!ValidationUtil.isValidEmail(email)) {
            req.setAttribute("alert", "Email không hợp lệ.");
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }
        if (!ValidationUtil.isNotBlank(fullname)) {
            req.setAttribute("alert", "Họ tên không được để trống.");
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }
        if (ValidationUtil.isNotBlank(phone) && !ValidationUtil.isValidPhone(phone)) {
            req.setAttribute("alert", "Số điện thoại không hợp lệ.");
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }
        if (userService.checkExistUsername(username.trim())) {
            req.setAttribute("alert", "Username đã tồn tại.");
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }
        if (userService.checkExistEmail(email.trim())) {
            req.setAttribute("alert", "Email đã được sử dụng.");
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }

        boolean ok = userService.register(username.trim(), password, email.trim(),
                fullname.trim(), phone != null ? phone.trim() : null);
        if (ok) {
            req.setAttribute("email", email.trim());
            req.setAttribute("alert", "Đăng ký thành công! Vui lòng nhập OTP đã gửi tới email để kích hoạt.");
            req.getRequestDispatcher("/views/activate.jsp").forward(req, resp);
        } else {
            req.setAttribute("alert", "Đăng ký thất bại. Vui lòng thử lại.");
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
        }
    }
}
