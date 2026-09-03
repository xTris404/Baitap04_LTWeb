<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Đăng nhập</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>
<div class="page-center">
<div class="auth-box">
    <form action="${pageContext.request.contextPath}/login" method="post">
        <h2>Đăng Nhập Vào Hệ Thống</h2>

        <c:if test="${alert != null}">
            <h3 class="alert alert-danger">${alert}</h3>
        </c:if>

        <section>
            <label>Tài khoản</label>
            <input type="text" name="username" placeholder="Tài khoản" class="form-control" required>
        </section>

        <section>
            <label>Mật khẩu</label>
            <input type="password" name="password" placeholder="Mật khẩu" class="form-control" required>
        </section>

        <section class="remember-row">
            <label><input type="checkbox" name="remember"> Nhớ tôi</label>
            <a href="#">Quên mật khẩu?</a>
        </section>

        <button type="submit" class="btn-submit">Đăng nhập</button>

        <p class="switch-link">
            Nếu bạn chưa có tài khoản trên hệ thống, thì hãy
            <a href="${pageContext.request.contextPath}/register">Đăng ký</a>
        </p>
    </form>
</div>
</div>
</body>
</html>
