package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.constants.Constant;
import vn.iotstar.entity.User;
import vn.iotstar.service.IUserService;
import vn.iotstar.service.UserServiceImpl;

@WebServlet(urlPatterns = { "/login" })
public class LoginController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final IUserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1) Nếu đã có Session đăng nhập rồi -> vào thẳng trang chủ
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute(Constant.SESSION_ACCOUNT) != null) {
            resp.sendRedirect(req.getContextPath() + "/waiting");
            return;
        }

        // 2) Chưa có Session -> kiểm tra Cookie "Remember me"
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(Constant.COOKIE_REMEMBER)) {
                    String username = cookie.getValue();
                    User user = userService.findByUsername(username);
                    if (user != null) {
                        session = req.getSession(true);
                        session.setAttribute(Constant.SESSION_ACCOUNT, user);
                        resp.sendRedirect(req.getContextPath() + "/waiting");
                        return;
                    }
                }
            }
        }

        // 3) Không có Session, không có Cookie hợp lệ -> hiển thị form đăng nhập
        req.getRequestDispatcher(Constant.Path.LOGIN).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String remember = req.getParameter("remember");
        boolean isRememberMe = "on".equals(remember);

        String alertMsg = "";

        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            alertMsg = "Tài khoản hoặc mật khẩu không được để rỗng";
            req.setAttribute("alert", alertMsg);
            req.getRequestDispatcher(Constant.Path.LOGIN).forward(req, resp);
            return;
        }

        // Gọi tầng Service (Service -> DAO -> JPA/EntityManager -> Database)
        User user = userService.login(username, password);

        if (user != null) {
            HttpSession session = req.getSession(true);
            session.setAttribute(Constant.SESSION_ACCOUNT, user);

            if (isRememberMe) {
                saveRememberMeCookie(resp, username);
            }
            resp.sendRedirect(req.getContextPath() + "/waiting");
        } else {
            alertMsg = "Tài khoản hoặc mật khẩu không đúng";
            req.setAttribute("alert", alertMsg);
            req.getRequestDispatcher(Constant.Path.LOGIN).forward(req, resp);
        }
    }

    /** Lưu username vào Cookie phía trình duyệt để lần sau tự động đăng nhập */
    private void saveRememberMeCookie(HttpServletResponse response, String username) {
        Cookie cookie = new Cookie(Constant.COOKIE_REMEMBER, username);
        cookie.setMaxAge(Constant.COOKIE_MAX_AGE);
        cookie.setPath("/"); // cookie có hiệu lực cho toàn bộ ứng dụng
        response.addCookie(cookie);
    }
}
