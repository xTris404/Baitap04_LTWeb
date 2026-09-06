<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Sửa sản phẩm</title>
</head>
<body>
<h3 class="mb-3">Sửa sản phẩm</h3>
<c:if test="${not empty alert}">
    <div class="alert alert-danger">${alert}</div>
</c:if>
<form class="needs-validation" novalidate method="post"
      action="${pageContext.request.contextPath}/admin/product/update"
      enctype="multipart/form-data">
    <input type="hidden" name="id" value="${product.id}">
    <div class="mb-3">
        <label class="form-label">Tên sản phẩm *</label>
        <input type="text" name="name" class="form-control" value="${product.name}" required>
        <div class="invalid-feedback">Bắt buộc.</div>
    </div>
    <div class="mb-3">
        <label class="form-label">Mô tả</label>
        <textarea name="description" class="form-control" rows="3">${product.description}</textarea>
    </div>
    <div class="row">
        <div class="col-md-4 mb-3">
            <label class="form-label">Giá *</label>
            <input type="number" name="price" class="form-control" value="${product.price}" required min="0" step="0.01">
            <div class="invalid-feedback">Giá hợp lệ.</div>
        </div>
        <div class="col-md-4 mb-3">
            <label class="form-label">Số lượng *</label>
            <input type="number" name="quantity" class="form-control" value="${product.quantity}" required min="0">
            <div class="invalid-feedback">Số lượng hợp lệ.</div>
        </div>
        <div class="col-md-4 mb-3">
            <label class="form-label">Trạng thái</label>
            <select name="status" class="form-select">
                <option value="1" ${product.status == 1 ? 'selected' : ''}>Đang bán</option>
                <option value="0" ${product.status == 0 ? 'selected' : ''}>Ngừng bán</option>
            </select>
        </div>
    </div>
    <div class="mb-3">
        <label class="form-label">Danh mục *</label>
        <select name="categoryId" class="form-select" required>
            <c:forEach items="${categories}" var="c">
                <option value="${c.id}" ${product.category.id == c.id ? 'selected' : ''}>${c.name}</option>
            </c:forEach>
        </select>
    </div>
    <div class="mb-3">
        <label class="form-label">Ảnh hiện tại</label><br>
        <c:if test="${not empty product.image}">
            <img src="${pageContext.request.contextPath}/image?fname=${product.image}"
                 alt="" style="height:80px;object-fit:cover;" class="mb-2">
        </c:if>
        <input type="file" name="imageFile" class="form-control" accept="image/*">
        <div class="form-text">Để trống nếu giữ ảnh cũ.</div>
    </div>
    <button type="submit" class="btn btn-primary">Cập nhật</button>
    <a href="${pageContext.request.contextPath}/admin/products" class="btn btn-secondary">Hủy</a>
</form>
</body>
</html>
