<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đăng nhập</title>
</head>
<body>
<div class="row justify-content-center">
    <div class="col-md-5">
        <div class="card shadow-sm">
            <div class="card-body p-4">
                <h3 class="card-title text-center mb-4">Đăng nhập</h3>
                <c:if test="${not empty alert}">
                    <div class="alert alert-danger">${alert}</div>
                </c:if>
                <c:if test="${not empty success}">
                    <div class="alert alert-success">${success}</div>
                </c:if>
                <form class="needs-validation" novalidate method="post"
                      action="${pageContext.request.contextPath}/login">
                    <div class="mb-3">
                        <label class="form-label">Username</label>
                        <input type="text" name="username" class="form-control"
                               value="${username}" required minlength="3" maxlength="50"
                               pattern="[a-zA-Z0-9_]+">
                        <div class="invalid-feedback">Username 3-50 ký tự (chữ, số, _).</div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Mật khẩu</label>
                        <input type="password" name="password" class="form-control" required minlength="6">
                        <div class="invalid-feedback">Mật khẩu tối thiểu 6 ký tự.</div>
                    </div>
                    <div class="mb-3 form-check">
                        <input type="checkbox" name="remember" class="form-check-input" id="remember">
                        <label class="form-check-label" for="remember">Ghi nhớ đăng nhập</label>
                    </div>
                    <button type="submit" class="btn btn-primary w-100">Đăng nhập</button>
                </form>
                <div class="mt-3 text-center small">
                    <a href="${pageContext.request.contextPath}/forgot-password">Quên mật khẩu?</a>
                    &nbsp;|&nbsp;
                    <a href="${pageContext.request.contextPath}/register">Đăng ký</a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
