<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Sửa danh mục</title>
</head>
<body>
<h3 class="mb-3">Sửa danh mục</h3>
<form class="needs-validation" novalidate method="post"
      action="<c:url value='/admin/category/update'/>" enctype="multipart/form-data">
    <input type="hidden" name="id" value="${cate.id}">
    <div class="mb-3">
        <label class="form-label">Tên danh mục *</label>
        <input type="text" name="name" class="form-control" value="${cate.name}" required>
        <div class="invalid-feedback">Tên danh mục bắt buộc.</div>
    </div>
    <div class="mb-3">
        <label class="form-label">Ảnh hiện tại</label><br>
        <c:if test="${not empty cate.icon}">
            <img src="<c:url value='/image?fname=${cate.icon}'/>" alt="" style="height:60px;" class="mb-2">
        </c:if>
        <input type="file" name="iconFile" class="form-control" accept="image/*">
        <div class="form-text">Để trống nếu giữ ảnh cũ.</div>
    </div>
    <div class="mb-3">
        <label class="form-label">Trạng thái</label><br>
        <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" name="status" value="1" id="st1"
                   ${cate.status == 1 ? 'checked' : ''}>
            <label class="form-check-label" for="st1">Hoạt động</label>
        </div>
        <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" name="status" value="0" id="st0"
                   ${cate.status == 0 ? 'checked' : ''}>
            <label class="form-check-label" for="st0">Khóa</label>
        </div>
    </div>
    <button type="submit" class="btn btn-primary">Cập nhật</button>
    <a href="<c:url value='/admin/categories'/>" class="btn btn-secondary">Hủy</a>
</form>
</body>
</html>
