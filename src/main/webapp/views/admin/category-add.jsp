<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Thêm danh mục</title>
</head>
<body>
<h3 class="mb-3">Thêm danh mục</h3>
<form class="needs-validation" novalidate method="post"
      action="<c:url value='/admin/category/insert'/>" enctype="multipart/form-data">
    <div class="mb-3">
        <label class="form-label">Tên danh mục *</label>
        <input type="text" name="name" class="form-control" required>
        <div class="invalid-feedback">Tên danh mục bắt buộc.</div>
    </div>
    <div class="mb-3">
        <label class="form-label">Ảnh đại diện</label>
        <input type="file" name="iconFile" class="form-control" accept="image/*">
    </div>
    <div class="mb-3">
        <label class="form-label">Trạng thái</label><br>
        <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" name="status" value="1" id="st1" checked>
            <label class="form-check-label" for="st1">Hoạt động</label>
        </div>
        <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" name="status" value="0" id="st0">
            <label class="form-check-label" for="st0">Khóa</label>
        </div>
    </div>
    <button type="submit" class="btn btn-primary">Thêm</button>
    <a href="<c:url value='/admin/categories'/>" class="btn btn-secondary">Hủy</a>
</form>
</body>
</html>
