package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.service.IUserService;
import vn.iotstar.service.UserServiceImpl;
import vn.iotstar.utils.ValidationUtil;

@WebServlet(urlPatterns = { "/reset-password" })
public class ResetPasswordController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final IUserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email = req.getParameter("email");
        if (email != null) {
            req.setAttribute("email", email);
        }
        req.getRequestDispatcher("/views/reset-password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String email = req.getParameter("email");
        String otp = req.getParameter("otp");
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirm");

        req.setAttribute("email", email);

        if (!ValidationUtil.isValidEmail(email)) {
            req.setAttribute("alert", "Email không hợp lệ.");
            req.getRequestDispatcher("/views/reset-password.jsp").forward(req, resp);
            return;
        }
        if (!ValidationUtil.isValidOtp(otp)) {
            req.setAttribute("alert", "OTP phải là 6 chữ số.");
            req.getRequestDispatcher("/views/reset-password.jsp").forward(req, resp);
            return;
        }
        if (!ValidationUtil.isValidPassword(password)) {
            req.setAttribute("alert", "Mật khẩu tối thiểu 6 ký tự.");
            req.getRequestDispatcher("/views/reset-password.jsp").forward(req, resp);
            return;
        }
        if (confirm == null || !password.equals(confirm)) {
            req.setAttribute("alert", "Mật khẩu xác nhận không khớp.");
            req.getRequestDispatcher("/views/reset-password.jsp").forward(req, resp);
            return;
        }

        boolean ok = userService.resetPassword(email.trim(), otp.trim(), password);
        if (ok) {
            req.setAttribute("success", "Đặt lại mật khẩu thành công! Bạn có thể đăng nhập.");
            req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
        } else {
            req.setAttribute("alert", "OTP không đúng hoặc đã hết hạn.");
            req.getRequestDispatcher("/views/reset-password.jsp").forward(req, resp);
        }
    }
}
