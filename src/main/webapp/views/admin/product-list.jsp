<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quản lý sản phẩm</title>
</head>
<body>
<div class="d-flex justify-content-between align-items-center mb-3">
    <h3 class="mb-0">Quản lý sản phẩm</h3>
    <a href="${pageContext.request.contextPath}/admin/product/add" class="btn btn-primary">+ Thêm sản phẩm</a>
</div>

<form class="row g-2 mb-3" method="get" action="${pageContext.request.contextPath}/admin/products">
    <div class="col-auto">
        <input type="text" name="keyword" class="form-control" placeholder="Tìm theo tên..." value="${keyword}">
    </div>
    <div class="col-auto">
        <button type="submit" class="btn btn-outline-secondary">Tìm</button>
    </div>
</form>

<div class="table-responsive">
    <table class="table table-bordered table-hover align-middle">
        <thead class="table-light">
        <tr>
            <th>ID</th>
            <th>Ảnh</th>
            <th>Tên</th>
            <th>Giá</th>
            <th>SL</th>
            <th>Danh mục</th>
            <th>Trạng thái</th>
            <th style="width:140px">Thao tác</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${listproduct}" var="p">
            <tr>
                <td>${p.id}</td>
                <td>
                    <c:if test="${not empty p.image}">
                        <img src="${pageContext.request.contextPath}/image?fname=${p.image}"
                             alt="" style="height:40px;width:40px;object-fit:cover;">
                    </c:if>
                </td>
                <td>${p.name}</td>
                <td>${p.price}</td>
                <td>${p.quantity}</td>
                <td>${p.category.name}</td>
                <td>${p.status == 1 ? 'Đang bán' : 'Ngừng bán'}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/admin/product/edit?id=${p.id}"
                       class="btn btn-sm btn-warning">Sửa</a>
                    <a href="${pageContext.request.contextPath}/admin/product/delete?id=${p.id}"
                       class="btn btn-sm btn-danger"
                       onclick="return confirm('Xóa sản phẩm này?');">Xóa</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty listproduct}">
            <tr><td colspan="8" class="text-center text-muted">Không có dữ liệu</td></tr>
        </c:if>
        </tbody>
    </table>
</div>
</body>
</html>
