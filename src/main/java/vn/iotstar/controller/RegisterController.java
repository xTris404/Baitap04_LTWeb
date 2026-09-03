package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.constants.Constant;
import vn.iotstar.service.IUserService;
import vn.iotstar.service.UserServiceImpl;

@WebServlet(urlPatterns = { "/register" })
public class RegisterController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final IUserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute(Constant.SESSION_ACCOUNT) != null) {
            resp.sendRedirect(req.getContextPath() + "/waiting");
            return;
        }
        req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String repassword = req.getParameter("repassword");
        String email = req.getParameter("email");
        String fullname = req.getParameter("fullname");
        String phone = req.getParameter("phone");

        String alertMsg;

        if (isEmpty(username) || isEmpty(password) || isEmpty(email)) {
            alertMsg = "Vui lòng nhập đầy đủ thông tin bắt buộc";
            forwardWithAlert(req, resp, alertMsg);
            return;
        }

        if (!password.equals(repassword)) {
            alertMsg = "Mật khẩu nhập lại không khớp";
            forwardWithAlert(req, resp, alertMsg);
            return;
        }

        if (userService.checkExistUsername(username)) {
            alertMsg = "Tài khoản đã tồn tại!";
            forwardWithAlert(req, resp, alertMsg);
            return;
        }

        if (userService.checkExistEmail(email)) {
            alertMsg = "Email đã tồn tại!";
            forwardWithAlert(req, resp, alertMsg);
            return;
        }

        boolean isSuccess = userService.register(username, password, email, fullname, phone);

        if (isSuccess) {
            req.setAttribute("alert", "Tạo tài khoản thành công! Vui lòng đăng nhập.");
            resp.sendRedirect(req.getContextPath() + "/login");
        } else {
            forwardWithAlert(req, resp, "Có lỗi hệ thống, vui lòng thử lại!");
        }
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    private void forwardWithAlert(HttpServletRequest req, HttpServletResponse resp, String alertMsg)
            throws ServletException, IOException {
        req.setAttribute("alert", alertMsg);
        req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
    }
}
