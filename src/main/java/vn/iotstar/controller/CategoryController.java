package vn.iotstar.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import vn.iotstar.constants.Constant;
import vn.iotstar.entity.Category;
import vn.iotstar.service.CategoryServiceImpl;
import vn.iotstar.service.ICategoryService;

/**
 * Gom toàn bộ CRUD của Category vào 1 servlet, phân nhánh theo URI —
 * cùng phong cách với LoginController/RegisterController trong project này.
 *
 * Upload ảnh dùng Part API chuẩn của Jakarta Servlet (không dùng
 * commons-fileupload vì bản 1.4 build trên javax.servlet, không tương thích
 * với jakarta.servlet-api mà project đang dùng).
 */
@WebServlet(urlPatterns = {
        "/admin/categories",
        "/admin/category/add",
        "/admin/category/insert",
        "/admin/category/edit",
        "/admin/category/update",
        "/admin/category/delete"
})
@MultipartConfig(
        maxFileSize = 5L * 1024 * 1024,       // 5MB / file
        maxRequestSize = 10L * 1024 * 1024,   // 10MB / request
        fileSizeThreshold = 1024 * 1024       // >1MB thì mới ghi ra temp file
)
public class CategoryController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ICategoryService categoryService = new CategoryServiceImpl();

    // Danh mục con trong Constant.DIR để chứa ảnh category, tách khỏi các loại upload khác
    private static final String SUB_DIR = "category";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String url = req.getRequestURI();

        if (url.contains("/admin/categories")) {
            String keyword = req.getParameter("keyword");
            List<Category> list = (keyword == null || keyword.trim().isEmpty())
                    ? categoryService.findAll()
                    : categoryService.searchByName(keyword.trim());
            req.setAttribute("listcate", list);
            req.setAttribute("keyword", keyword);
            req.getRequestDispatcher("/views/admin/category-list.jsp").forward(req, resp);

        } else if (url.contains("/admin/category/add")) {
            req.getRequestDispatcher("/views/admin/category-add.jsp").forward(req, resp);

        } else if (url.contains("/admin/category/edit")) {
            int id = Integer.parseInt(req.getParameter("id"));
            Category category = categoryService.findById(id);
            if (category == null) {
                resp.sendRedirect(req.getContextPath() + "/admin/categories");
                return;
            }
            req.setAttribute("cate", category);
            req.getRequestDispatcher("/views/admin/category-edit.jsp").forward(req, resp);

        } else if (url.contains("/admin/category/delete")) {
            int id = Integer.parseInt(req.getParameter("id"));
            try {
                Category category = categoryService.findById(id);
                categoryService.delete(id);
                if (category != null) {
                    deleteLocalFileIfAny(category.getIcon());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            resp.sendRedirect(req.getContextPath() + "/admin/categories");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String url = req.getRequestURI();

        if (url.contains("/admin/category/insert")) {
            handleInsert(req, resp);
        } else if (url.contains("/admin/category/update")) {
            handleUpdate(req, resp);
        }
    }

    private void handleInsert(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        String name = req.getParameter("name");
        int status = parseStatus(req.getParameter("status"));

        Category category = new Category(name, null, status);

        String savedFileName = saveUploadedFileIfAny(req, "iconFile");
        if (savedFileName != null) {
            category.setIcon(savedFileName);
        }

        categoryService.insert(category);
        resp.sendRedirect(req.getContextPath() + "/admin/categories");
    }

    private void handleUpdate(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        int id = Integer.parseInt(req.getParameter("id"));
        Category category = categoryService.findById(id);
        if (category == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/categories");
            return;
        }

        category.setName(req.getParameter("name"));
        category.setStatus(parseStatus(req.getParameter("status")));

        String savedFileName = saveUploadedFileIfAny(req, "iconFile");
        if (savedFileName != null) {
            // Có ảnh mới -> xoá ảnh cũ trên đĩa (nếu ảnh cũ do server lưu, không phải link http)
            deleteLocalFileIfAny(category.getIcon());
            category.setIcon(savedFileName);
        }
        // Không chọn file mới -> giữ nguyên icon cũ, không đụng vào

        categoryService.update(category);
        resp.sendRedirect(req.getContextPath() + "/admin/categories");
    }

    /**
     * Đọc Part tên fieldName từ request; nếu người dùng có chọn file (size > 0)
     * thì ghi ra Constant.DIR/category/ và trả về đường dẫn tương đối để lưu
     * vào cột icon (vd "category/1735289000000.jpg"). Trả về null nếu không
     * có file nào được chọn.
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
        String ext = dotIndex >= 0 ? originalFileName.substring(dotIndex + 1) : "";
        String fileName = System.currentTimeMillis() + (ext.isEmpty() ? "" : "." + ext);

        File uploadDir = new File(Constant.DIR, SUB_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        File target = new File(uploadDir, fileName);
        part.write(target.getAbsolutePath());

        return SUB_DIR + "/" + fileName;
    }

    /** Xoá file vật lý ứng với icon, nếu icon là file do server lưu (không phải URL http/https). */
    private void deleteLocalFileIfAny(String icon) {
        if (icon == null || icon.trim().isEmpty()) {
            return;
        }
        if (icon.startsWith("http://") || icon.startsWith("https://")) {
            return; // link ngoài, không có gì để xoá trên đĩa
        }
        try {
            File file = new File(Constant.DIR, icon);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** status luôn có giá trị mặc định 1 (hoạt động) nếu form không gửi lên */
    private int parseStatus(String raw) {
        try {
            return Integer.parseInt(raw);
        } catch (Exception e) {
            return 1;
        }
    }
}
