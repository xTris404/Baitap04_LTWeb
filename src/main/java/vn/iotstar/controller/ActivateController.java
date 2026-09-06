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

@WebServlet(urlPatterns = { "/activate", "/resend-otp" })
public class ActivateController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final IUserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email = req.getParameter("email");
        if (email != null) {
            req.setAttribute("email", email);
        }
        req.getRequestDispatcher("/views/activate.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String path = req.getServletPath();

        if ("/resend-otp".equals(path)) {
            handleResend(req, resp);
            return;
        }

        String email = req.getParameter("email");
        String otp = req.getParameter("otp");
        req.setAttribute("email", email);

        if (!ValidationUtil.isValidEmail(email)) {
            req.setAttribute("alert", "Email không hợp lệ.");
            req.getRequestDispatcher("/views/activate.jsp").forward(req, resp);
            return;
        }
        if (!ValidationUtil.isValidOtp(otp)) {
            req.setAttribute("alert", "OTP phải là 6 chữ số.");
            req.getRequestDispatcher("/views/activate.jsp").forward(req, resp);
            return;
        }

        boolean ok = userService.activateAccount(email.trim(), otp.trim());
        if (ok) {
            req.setAttribute("success", "Kích hoạt thành công! Bạn có thể đăng nhập.");
            req.getRequestDispatcher("/views/login.jsp").forward(req, resp);
        } else {
            req.setAttribute("alert", "OTP không đúng hoặc đã hết hạn. Vui lòng thử lại / gửi lại OTP.");
            req.getRequestDispatcher("/views/activate.jsp").forward(req, resp);
        }
    }

    private void handleResend(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email = req.getParameter("email");
        req.setAttribute("email", email);
        if (!ValidationUtil.isValidEmail(email)) {
            req.setAttribute("alert", "Email không hợp lệ.");
            req.getRequestDispatcher("/views/activate.jsp").forward(req, resp);
            return;
        }
        boolean ok = userService.resendActivationOtp(email.trim());
        if (ok) {
            req.setAttribute("success", "Đã gửi lại OTP tới email của bạn.");
        } else {
            req.setAttribute("alert", "Không thể gửi lại OTP (tài khoản không tồn tại hoặc đã kích hoạt).");
        }
        req.getRequestDispatcher("/views/activate.jsp").forward(req, resp);
    }
}
