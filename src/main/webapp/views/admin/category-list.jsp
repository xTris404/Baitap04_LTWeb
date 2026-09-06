<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quản lý danh mục</title>
</head>
<body>
<div class="d-flex justify-content-between align-items-center mb-3">
    <h3 class="mb-0">Quản lý danh mục</h3>
    <a href="<c:url value='/admin/category/add'/>" class="btn btn-primary">+ Thêm danh mục</a>
</div>
<form class="row g-2 mb-3" method="get" action="<c:url value='/admin/categories'/>">
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
            <th>Icon</th>
            <th>Tên</th>
            <th>Trạng thái</th>
            <th style="width:140px">Thao tác</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${listcate}" var="c">
            <tr>
                <td>${c.id}</td>
                <td>
                    <c:if test="${not empty c.icon}">
                        <img src="<c:url value='/image?fname=${c.icon}'/>" alt="" style="height:40px;">
                    </c:if>
                </td>
                <td>${c.name}</td>
                <td>${c.status == 1 ? 'Hoạt động' : 'Khóa'}</td>
                <td>
                    <a href="<c:url value='/admin/category/edit?id=${c.id}'/>" class="btn btn-sm btn-warning">Sửa</a>
                    <a href="<c:url value='/admin/category/delete?id=${c.id}'/>" class="btn btn-sm btn-danger"
                       onclick="return confirm('Xóa danh mục này?');">Xóa</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty listcate}">
            <tr><td colspan="5" class="text-center text-muted">Không có dữ liệu</td></tr>
        </c:if>
        </tbody>
    </table>
</div>
</body>
</html>
