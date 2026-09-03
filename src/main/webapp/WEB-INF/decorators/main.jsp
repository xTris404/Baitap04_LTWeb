<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%--
    LƯU Ý: SiteMesh 3.2.x KHÔNG còn dùng taglib JSP kiểu SiteMesh 2
    ("decorator:title/head/body" với uri opensymphony.com) nữa - taglib đó
    không tồn tại trong jar 3.2.x nên khai báo <%@ taglib %> sẽ làm Jasper
    lỗi biên dịch ("cannot be resolved..."). Từ SiteMesh 3 trở đi, các thẻ
    <sitemesh:write property="title|head|body"/> KHÔNG phải là JSP taglib
    thật sự - chúng chỉ là text thường được in ra trong HTML kết quả, rồi bộ
    xử lý riêng của SiteMesh (HTML TagProcessor) quét & thay thế SAU KHI JSP
    đã render xong. Vì vậy không cần (và không được) khai báo taglib cho
    prefix "sitemesh" bên dưới.
--%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><sitemesh:write property="title"/></title>
    <link rel="stylesheet" href="<c:url value='/assets/css/style.css'/>">
    <%-- Gộp thêm các thẻ trong <head> của trang gốc (nếu có) --%>
    <sitemesh:write property="head"/>
</head>
<body>
<header class="app-header">
    <div class="header-inner">
        <a href="<c:url value='/waiting'/>" class="brand">JPA Login Demo</a>
        <nav>
            <c:choose>
                <c:when test="${not empty sessionScope.account}">
                    <span class="hello-text">
                        Xin chào, ${not empty sessionScope.account.fullName ? sessionScope.account.fullName : sessionScope.account.userName}
                    </span>
                    <a href="<c:url value='/waiting'/>">Trang chủ</a>
                    <a href="<c:url value='/profile'/>">Hồ sơ cá nhân</a>
                    <c:if test="${sessionScope.account.roleid == 1}">
                        <a href="<c:url value='/admin/categories'/>">Quản lý danh mục</a>
                    </c:if>
                    <a href="<c:url value='/logout'/>">Đăng xuất</a>
                </c:when>
                <c:otherwise>
                    <a href="<c:url value='/login'/>">Đăng nhập</a>
                    <a href="<c:url value='/register'/>">Đăng ký</a>
                </c:otherwise>
            </c:choose>
        </nav>
    </div>
</header>

<main class="page-content">
    <%-- Nội dung <body> của trang gốc (login.jsp, home.jsp, profile.jsp, ...) --%>
    <sitemesh:write property="body"/>
</main>

<footer class="app-footer">
    &copy; 2026 jpa-login-demo &mdash; giao diện được quản lý dùng chung bởi SiteMesh
</footer>
</body>
</html>
