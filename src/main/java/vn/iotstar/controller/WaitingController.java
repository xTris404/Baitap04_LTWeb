package vn.iotstar.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.constants.Constant;
import vn.iotstar.entity.User;

@WebServlet(urlPatterns = { "/waiting" })
public class WaitingController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        if (session != null && session.getAttribute(Constant.SESSION_ACCOUNT) != null) {
            User user = (User) session.getAttribute(Constant.SESSION_ACCOUNT);
            req.setAttribute("username", user.getUserName());

            if (user.getRoleid() == 1) {
                resp.sendRedirect(req.getContextPath() + "/admin/home");
            } else if (user.getRoleid() == 2) {
                resp.sendRedirect(req.getContextPath() + "/manager/home");
            } else {
                req.getRequestDispatcher(Constant.Path.HOME).forward(req, resp);
            }
        } else {
            resp.sendRedirect(req.getContextPath() + "/login");
        }
    }
}
