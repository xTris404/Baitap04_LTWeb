package vn.iotstar.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.Date;
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
import vn.iotstar.entity.Product;
import vn.iotstar.service.CategoryServiceImpl;
import vn.iotstar.service.ICategoryService;
import vn.iotstar.service.IProductService;
import vn.iotstar.service.ProductServiceImpl;
import vn.iotstar.utils.ValidationUtil;

@WebServlet(urlPatterns = {
        "/admin/products",
        "/admin/product/add",
        "/admin/product/edit",
        "/admin/product/delete",
        "/admin/product/insert",
        "/admin/product/update"
})
@MultipartConfig(maxFileSize = 1024 * 1024 * 5)
public class ProductController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final String SUB_DIR = "product";

    private final IProductService productService = new ProductServiceImpl();
    private final ICategoryService categoryService = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String url = req.getRequestURI();

        if (url.contains("/admin/products")) {
            String keyword = req.getParameter("keyword");
            List<Product> list = (keyword == null || keyword.trim().isEmpty())
                    ? productService.findAll()
                    : productService.searchByName(keyword.trim());
            req.setAttribute("listproduct", list);
            req.setAttribute("keyword", keyword);
            req.getRequestDispatcher("/views/admin/product-list.jsp").forward(req, resp);

        } else if (url.contains("/admin/product/add")) {
            req.setAttribute("categories", categoryService.findAll());
            req.getRequestDispatcher("/views/admin/product-add.jsp").forward(req, resp);

        } else if (url.contains("/admin/product/edit")) {
            int id = Integer.parseInt(req.getParameter("id"));
            Product product = productService.findById(id);
            if (product == null) {
                resp.sendRedirect(req.getContextPath() + "/admin/products");
                return;
            }
            req.setAttribute("product", product);
            req.setAttribute("categories", categoryService.findAll());
            req.getRequestDispatcher("/views/admin/product-edit.jsp").forward(req, resp);

        } else if (url.contains("/admin/product/delete")) {
            int id = Integer.parseInt(req.getParameter("id"));
            try {
                Product product = productService.findById(id);
                productService.delete(id);
                if (product != null) {
                    deleteLocalFileIfAny(product.getImage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            resp.sendRedirect(req.getContextPath() + "/admin/products");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String url = req.getRequestURI();

        if (url.contains("/admin/product/insert")) {
            handleInsert(req, resp);
        } else if (url.contains("/admin/product/update")) {
            handleUpdate(req, resp);
        }
    }

    private void handleInsert(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        String priceStr = req.getParameter("price");
        String quantityStr = req.getParameter("quantity");
        String categoryIdStr = req.getParameter("categoryId");
        int status = parseStatus(req.getParameter("status"));

        if (!ValidationUtil.isNotBlank(name) || !ValidationUtil.isValidNumber(priceStr)
                || !ValidationUtil.isValidNumber(quantityStr) || !ValidationUtil.isValidNumber(categoryIdStr)) {
            req.setAttribute("alert", "Vui lòng nhập đầy đủ và hợp lệ các trường bắt buộc.");
            req.setAttribute("categories", categoryService.findAll());
            req.getRequestDispatcher("/views/admin/product-add.jsp").forward(req, resp);
            return;
        }

        Category category = categoryService.findById(Integer.parseInt(categoryIdStr));
        if (category == null) {
            req.setAttribute("alert", "Danh mục không tồn tại.");
            req.setAttribute("categories", categoryService.findAll());
            req.getRequestDispatcher("/views/admin/product-add.jsp").forward(req, resp);
            return;
        }

        Product product = new Product();
        product.setName(name.trim());
        product.setDescription(description);
        product.setPrice(new BigDecimal(priceStr.trim()));
        product.setQuantity(Integer.parseInt(quantityStr.trim()));
        product.setStatus(status);
        product.setCreatedDate(new Date());
        product.setCategory(category);

        String savedFileName = saveUploadedFileIfAny(req, "imageFile");
        if (savedFileName != null) {
            product.setImage(savedFileName);
        }

        productService.insert(product);
        resp.sendRedirect(req.getContextPath() + "/admin/products");
    }

    private void handleUpdate(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        int id = Integer.parseInt(req.getParameter("id"));
        Product product = productService.findById(id);
        if (product == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/products");
            return;
        }

        String name = req.getParameter("name");
        String description = req.getParameter("description");
        String priceStr = req.getParameter("price");
        String quantityStr = req.getParameter("quantity");
        String categoryIdStr = req.getParameter("categoryId");
        int status = parseStatus(req.getParameter("status"));

        if (!ValidationUtil.isNotBlank(name) || !ValidationUtil.isValidNumber(priceStr)
                || !ValidationUtil.isValidNumber(quantityStr) || !ValidationUtil.isValidNumber(categoryIdStr)) {
            req.setAttribute("alert", "Vui lòng nhập đầy đủ và hợp lệ các trường bắt buộc.");
            req.setAttribute("product", product);
            req.setAttribute("categories", categoryService.findAll());
            req.getRequestDispatcher("/views/admin/product-edit.jsp").forward(req, resp);
            return;
        }

        Category category = categoryService.findById(Integer.parseInt(categoryIdStr));
        if (category == null) {
            req.setAttribute("alert", "Danh mục không tồn tại.");
            req.setAttribute("product", product);
            req.setAttribute("categories", categoryService.findAll());
            req.getRequestDispatcher("/views/admin/product-edit.jsp").forward(req, resp);
            return;
        }

        product.setName(name.trim());
        product.setDescription(description);
        product.setPrice(new BigDecimal(priceStr.trim()));
        product.setQuantity(Integer.parseInt(quantityStr.trim()));
        product.setStatus(status);
        product.setCategory(category);

        String savedFileName = saveUploadedFileIfAny(req, "imageFile");
        if (savedFileName != null) {
            deleteLocalFileIfAny(product.getImage());
            product.setImage(savedFileName);
        }

        productService.update(product);
        resp.sendRedirect(req.getContextPath() + "/admin/products");
    }

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

    private void deleteLocalFileIfAny(String image) {
        if (image == null || image.trim().isEmpty()) {
            return;
        }
        if (image.startsWith("http://") || image.startsWith("https://")) {
            return;
        }
        try {
            File file = new File(Constant.DIR, image);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int parseStatus(String raw) {
        try {
            return Integer.parseInt(raw);
        } catch (Exception e) {
            return 1;
        }
    }
}
