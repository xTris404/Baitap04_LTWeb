<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sửa danh mục</title>
</head>
<body>
    <h2>Sửa danh mục</h2>

    <form action="<c:url value='/admin/category/update'/>" method="post" enctype="multipart/form-data">
        <input type="hidden" name="id" value="${cate.id}">

        <div>
            <label>Tên danh mục:</label><br>
            <input type="text" name="name" value="${cate.name}" required>
        </div>
        <br>
        <div>
            <label>Ảnh hiện tại:</label><br>
            <c:if test="${not empty cate.icon}">
                <c:choose>
                    <c:when test="${fn:startsWith(cate.icon, 'http')}">
                        <c:url value="${cate.icon}" var="imgUrl"/>
                    </c:when>
                    <c:otherwise>
                        <c:url value="/image?fname=${cate.icon}" var="imgUrl"/>
                    </c:otherwise>
                </c:choose>
                <br>
                <img src="${imgUrl}" width="150" alt="">
            </c:if>
            <c:if test="${empty cate.icon}">
                <em>Chưa có ảnh</em>
            </c:if>
            <br><br>
            <label>Chọn ảnh mới (bỏ trống nếu giữ ảnh cũ):</label><br>
            <input type="file" name="iconFile" accept="image/*">
        </div>
        <br>
        <div>
            <label>Trạng thái:</label><br>
            <input type="radio" name="status" value="1" ${cate.status == 1 ? 'checked' : ''}> Hoạt động
            <input type="radio" name="status" value="0" ${cate.status != 1 ? 'checked' : ''}> Khóa
        </div>
        <br>
        <button type="submit">Cập nhật</button>
        <a href="<c:url value='/admin/categories'/>">Hủy</a>
    </form>
</body>
</html>
