package vn.iotstar.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.constants.Constant;

/**
 * Trả nội dung 1 file trong thư mục upload (Constant.DIR) ra ngoài trình
 * duyệt, dùng cho thẻ <img src="/image?fname=category/xxx.jpg">.
 *
 * Có kiểm tra path traversal: nếu fname chứa "../" để trỏ ra ngoài
 * Constant.DIR (vd fname=../../WEB-INF/web.xml) thì từ chối, thay vì đọc và
 * trả về bất kỳ file nào trên server như bản gốc trong tài liệu.
 */
@WebServlet(urlPatterns = "/image") // ?fname=category/xxx.jpg
public class DownloadImageController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String fname = req.getParameter("fname");
        if (fname == null || fname.trim().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Path baseDir = Paths.get(Constant.DIR).toAbsolutePath().normalize();
        Path target = baseDir.resolve(fname).normalize();

        // Chặn path traversal: file thực tế phải nằm bên trong baseDir
        if (!target.startsWith(baseDir)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        File file = target.toFile();
        if (!file.exists() || !file.isFile()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String contentType = Files.probeContentType(target);
        resp.setContentType(contentType != null ? contentType : "application/octet-stream");

        try (InputStream in = Files.newInputStream(target);
             OutputStream out = resp.getOutputStream()) {
            in.transferTo(out);
        }
    }
}
