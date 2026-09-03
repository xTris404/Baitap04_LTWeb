<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thêm danh mục</title>
</head>
<body>
    <h2>Thêm danh mục</h2>

    <form action="<c:url value='/admin/category/insert'/>" method="post" enctype="multipart/form-data">
        <div>
            <label>Tên danh mục:</label><br>
            <input type="text" name="name" required>
        </div>
        <br>
        <div>
            <label>Ảnh đại diện:</label><br>
            <input type="file" name="iconFile" accept="image/*">
        </div>
        <br>
        <div>
            <label>Trạng thái:</label><br>
            <input type="radio" name="status" value="1" checked> Hoạt động
            <input type="radio" name="status" value="0"> Khóa
        </div>
        <br>
        <button type="submit">Thêm</button>
        <a href="<c:url value='/admin/categories'/>">Hủy</a>
    </form>
</body>
</html>
