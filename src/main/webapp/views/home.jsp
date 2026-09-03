<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Trang chủ</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<div class="page-center">
<div class="auth-box">
    <h2>Xin chào, ${sessionScope.account.fullName != null ? sessionScope.account.fullName : sessionScope.account.userName}!</h2>
    <p>Bạn đã đăng nhập thành công bằng <b>Session</b>${cookie.username != null ? " (được ghi nhớ qua Cookie)" : ""}.</p>
    <p>Email: ${sessionScope.account.email}</p>
    <p><a href="${pageContext.request.contextPath}/profile">Hồ sơ cá nhân</a></p>
    <p><a href="${pageContext.request.contextPath}/admin/categories">Quản lý danh mục</a></p>
    <p><a href="${pageContext.request.contextPath}/logout">Đăng xuất</a></p>
</div>
</div>
</body>
</html>
