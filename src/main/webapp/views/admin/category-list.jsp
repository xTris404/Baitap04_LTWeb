<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản lý danh mục</title>
</head>
<body>
    <h2>Quản lý danh mục</h2>

    <a href="<c:url value='/admin/category/add'/>">+ Thêm danh mục</a>
    <br><br>

    <form action="<c:url value='/admin/categories'/>" method="get">
        <input type="text" name="keyword" value="${keyword}" placeholder="Tìm theo tên...">
        <button type="submit">Tìm</button>
    </form>
    <br>

    <c:if test="${empty listcate}">
        <p>Chưa có danh mục nào.</p>
    </c:if>

    <c:if test="${not empty listcate}">
        <table border="1" cellpadding="6" cellspacing="0" width="100%">
            <tr>
                <th>ID</th>
                <th>Icon</th>
                <th>Tên danh mục</th>
                <th>Trạng thái</th>
                <th>Hành động</th>
            </tr>
            <c:forEach items="${listcate}" var="cate">
                <tr>
                    <td>${cate.id}</td>
                    <td>
                        <c:if test="${not empty cate.icon}">
                            <c:choose>
                                <c:when test="${fn:startsWith(cate.icon, 'http')}">
                                    <c:url value="${cate.icon}" var="imgUrl"/>
                                </c:when>
                                <c:otherwise>
                                    <c:url value="/image?fname=${cate.icon}" var="imgUrl"/>
                                </c:otherwise>
                            </c:choose>
                            <img src="${imgUrl}" height="60" alt="">
                        </c:if>
                    </td>
                    <td>${cate.name}</td>
                    <td>
                        <c:choose>
                            <c:when test="${cate.status == 1}">Hoạt động</c:when>
                            <c:otherwise>Khóa</c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <a href="<c:url value='/admin/category/edit?id=${cate.id}'/>">Sửa</a>
                        |
                        <a href="<c:url value='/admin/category/delete?id=${cate.id}'/>"
                           onclick="return confirm('Xóa danh mục này?');">Xóa</a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
</body>
</html>
