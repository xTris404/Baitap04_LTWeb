package vn.iotstar.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import vn.iotstar.constants.Constant;
import vn.iotstar.entity.User;
import vn.iotstar.service.IUserService;
import vn.iotstar.service.UserServiceImpl;

/**
 * Chức năng "Hồ sơ cá nhân": cho phép user đã đăng nhập cập nhật fullname,
 * phone và ảnh đại diện (avatar). Cùng phong cách với CategoryController:
 * gom GET (xem hồ sơ) + POST (cập nhật) vào 1 servlet, upload ảnh bằng
 * Part API chuẩn của Jakarta Servlet (multipart/form-data), lưu file vật lý
 * vào Constant.DIR/avatar rồi lưu đường dẫn tương đối vào cột "avatar" qua
 * tầng Service -> DAO -> JPA (EntityManager.merge).
 *
 * Vì dùng redirect sau khi POST (Post/Redirect/Get) nên thông báo
 * thành công/lỗi được lưu tạm 1 lần vào Session ("flash message") rồi xoá đi
 * ngay khi trang GET /profile đọc xong, tương tự flash attribute của các
 * framework MVC khác.
 */
@WebServlet(urlPatterns = { "/profile", "/profile/update" })
@MultipartConfig(
        maxFileSize = 5L * 1024 * 1024,       // 5MB / file
        maxRequestSize = 10L * 1024 * 1024,   // 10MB / request
        fileSizeThreshold = 1024 * 1024       // >1MB thì mới ghi ra temp file
)
public class ProfileController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final IUserService userService = new UserServiceImpl();

    // Danh mục con trong Constant.DIR để chứa avatar, tách khỏi ảnh category
    private static final String SUB_DIR = "avatar";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User account = (session != null) ? (User) session.getAttribute(Constant.SESSION_ACCOUNT) : null;
        if (account == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Lấy lại dữ liệu mới nhất từ DB (tránh hiển thị dữ liệu cũ trong Session
        // nếu vừa cập nhật ở tab/thiết bị khác)
        User user = userService.findById(account.getId());
        if (user == null) {
            user = account;
        }
        req.setAttribute("user", user);

        // Đọc & xoá flash message (nếu có) từ lần POST /profile/update trước đó
        Object success = session.getAttribute("flashSuccess");
        Object error = session.getAttribute("flashError");
        if (success != null) {
            req.setAttribute("success", success);
            session.removeAttribute("flashSuccess");
        }
        if (error != null) {
            req.setAttribute("error", error);
            session.removeAttribute("flashError");
        }

        req.getRequestDispatcher(Constant.Path.PROFILE).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);
        User account = (session != null) ? (User) session.getAttribute(Constant.SESSION_ACCOUNT) : null;
        if (account == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String fullName = req.getParameter("fullname");
        String phone = req.getParameter("phone");
        if (phone != null) {
            phone = phone.trim();
        }

        if (fullName == null || fullName.trim().isEmpty()) {
            session.setAttribute("flashError", "Họ tên không được để trống");
            resp.sendRedirect(req.getContextPath() + "/profile");
            return;
        }
        fullName = fullName.trim();

        // Số điện thoại không được trùng với user KHÁC (được phép giữ nguyên số cũ của chính mình)
        boolean phoneChanged = phone != null && !phone.isEmpty() && !phone.equals(account.getPhone());
        if (phoneChanged && userService.checkExistPhone(phone)) {
            session.setAttribute("flashError", "Số điện thoại đã được sử dụng bởi tài khoản khác");
            resp.sendRedirect(req.getContextPath() + "/profile");
            return;
        }

        String oldAvatar = account.getAvatar();
        String savedFileName;
        try {
            savedFileName = saveUploadedFileIfAny(req, "avatarFile");
        } catch (IllegalArgumentException ex) {
            session.setAttribute("flashError", ex.getMessage());
            resp.sendRedirect(req.getContextPath() + "/profile");
            return;
        }

        User updated = userService.updateProfile(account.getId(), fullName, phone, savedFileName);
        if (updated == null) {
            session.setAttribute("flashError", "Không tìm thấy tài khoản, vui lòng đăng nhập lại");
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (savedFileName != null) {
            // Upload avatar mới thành công -> xoá file avatar cũ trên đĩa (nếu có)
            deleteLocalFileIfAny(oldAvatar);
        }

        // Đồng bộ lại thông tin trong Session để header/nav (SiteMesh decorator)
        // hiển thị đúng ngay lập tức, không cần đăng nhập lại
        session.setAttribute(Constant.SESSION_ACCOUNT, updated);
        session.setAttribute("flashSuccess", "Cập nhật hồ sơ thành công!");
        resp.sendRedirect(req.getContextPath() + "/profile");
    }

    /**
     * Đọc Part tên fieldName từ request; nếu người dùng có chọn file ảnh hợp lệ
     * thì ghi ra Constant.DIR/avatar/ và trả về đường dẫn tương đối để lưu vào
     * cột avatar (vd "avatar/1735289000000.jpg"). Trả về null nếu không có file
     * nào được chọn (giữ nguyên avatar cũ).
     */
    private String saveUploadedFileIfAny(HttpServletRequest req, String fieldName)
            throws IOException, ServletException {

        Part part = req.getPart(fieldName);
        if (part == null || part.getSize() <= 0) {
            return null;
        }

        String submitted = part.getSubmittedFileName();
        if (submitted == null || submitted.trim().isEmpty()) {
            return null;
        }

        String originalFileName = Paths.get(submitted).getFileName().toString();
        int dotIndex = originalFileName.lastIndexOf('.');
        String ext = dotIndex >= 0 ? originalFileName.substring(dotIndex + 1).toLowerCase() : "";

        if (!isAllowedImageExt(ext)) {
            throw new IllegalArgumentException("Ảnh đại diện chỉ chấp nhận định dạng JPG, JPEG, PNG, GIF hoặc WEBP");
        }

        String fileName = System.currentTimeMillis() + "." + ext;

        File uploadDir = new File(Constant.DIR, SUB_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        File target = new File(uploadDir, fileName);
        part.write(target.getAbsolutePath());

        return SUB_DIR + "/" + fileName;
    }

    private boolean isAllowedImageExt(String ext) {
        return ext.equals("jpg") || ext.equals("jpeg") || ext.equals("png")
                || ext.equals("gif") || ext.equals("webp");
    }

    /** Xoá file vật lý ứng với avatar, nếu avatar là file do server lưu (không phải URL http/https). */
    private void deleteLocalFileIfAny(String avatar) {
        if (avatar == null || avatar.trim().isEmpty()) {
            return;
        }
        if (avatar.startsWith("http://") || avatar.startsWith("https://")) {
            return; // link ngoài, không có gì để xoá trên đĩa
        }
        try {
            File file = new File(Constant.DIR, avatar);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
