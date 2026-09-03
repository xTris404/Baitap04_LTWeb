<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đăng ký</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<div class="page-center">
<div class="auth-box">
    <form action="${pageContext.request.contextPath}/register" method="post">
        <h2>Tạo Tài Khoản Mới</h2>

        <c:if test="${alert != null}">
            <h3 class="alert alert-danger">${alert}</h3>
        </c:if>

        <section>
            <label>Tài khoản</label>
            <input type="text" name="username" placeholder="Tài khoản" class="form-control" required>
        </section>

        <section>
            <label>Họ tên</label>
            <input type="text" name="fullname" placeholder="Họ tên" class="form-control">
        </section>

        <section>
            <label>Email</label>
            <input type="email" name="email" placeholder="Nhập Email" class="form-control" required>
        </section>

        <section>
            <label>Số điện thoại</label>
            <input type="text" name="phone" placeholder="Số điện thoại" class="form-control">
        </section>

        <section>
            <label>Mật khẩu</label>
            <input type="password" name="password" placeholder="Mật khẩu" class="form-control" required>
        </section>

        <section>
            <label>Nhập lại mật khẩu</label>
            <input type="password" name="repassword" placeholder="Nhập lại mật khẩu" class="form-control" required>
        </section>

        <button type="submit" class="btn-submit">Tạo tài khoản</button>

        <p class="switch-link">
            Nếu bạn đã có tài khoản?
            <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
        </p>
    </form>
</div>
</div>
</body>
</html>
