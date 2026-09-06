<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Thêm sản phẩm</title>
</head>
<body>
<h3 class="mb-3">Thêm sản phẩm</h3>
<c:if test="${not empty alert}">
    <div class="alert alert-danger">${alert}</div>
</c:if>
<form class="needs-validation" novalidate method="post"
      action="${pageContext.request.contextPath}/admin/product/insert"
      enctype="multipart/form-data">
    <div class="mb-3">
        <label class="form-label">Tên sản phẩm *</label>
        <input type="text" name="name" class="form-control" required>
        <div class="invalid-feedback">Bắt buộc.</div>
    </div>
    <div class="mb-3">
        <label class="form-label">Mô tả</label>
        <textarea name="description" class="form-control" rows="3"></textarea>
    </div>
    <div class="row">
        <div class="col-md-4 mb-3">
            <label class="form-label">Giá *</label>
            <input type="number" name="price" class="form-control" required min="0" step="0.01">
            <div class="invalid-feedback">Giá hợp lệ.</div>
        </div>
        <div class="col-md-4 mb-3">
            <label class="form-label">Số lượng *</label>
            <input type="number" name="quantity" class="form-control" required min="0">
            <div class="invalid-feedback">Số lượng hợp lệ.</div>
        </div>
        <div class="col-md-4 mb-3">
            <label class="form-label">Trạng thái</label>
            <select name="status" class="form-select">
                <option value="1" selected>Đang bán</option>
                <option value="0">Ngừng bán</option>
            </select>
        </div>
    </div>
    <div class="mb-3">
        <label class="form-label">Danh mục *</label>
        <select name="categoryId" class="form-select" required>
            <option value="">-- Chọn danh mục --</option>
            <c:forEach items="${categories}" var="c">
                <option value="${c.id}">${c.name}</option>
            </c:forEach>
        </select>
        <div class="invalid-feedback">Chọn danh mục.</div>
    </div>
    <div class="mb-3">
        <label class="form-label">Ảnh sản phẩm</label>
        <input type="file" name="imageFile" class="form-control" accept="image/*">
    </div>
    <button type="submit" class="btn btn-primary">Lưu</button>
    <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-secondary">Hủy</a>
</form>
</body>
</html>
