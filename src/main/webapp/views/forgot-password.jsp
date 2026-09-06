<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quên mật khẩu</title>
</head>
<body>
<div class="row justify-content-center">
    <div class="col-md-5">
        <div class="card shadow-sm">
            <div class="card-body p-4">
                <h3 class="card-title text-center mb-4">Quên mật khẩu</h3>
                <c:if test="${not empty alert}">
                    <div class="alert alert-danger">${alert}</div>
                </c:if>
                <form class="needs-validation" novalidate method="post"
                      action="${pageContext.request.contextPath}/forgot-password">
                    <div class="mb-3">
                        <label class="form-label">Email đã đăng ký</label>
                        <input type="email" name="email" class="form-control" value="${email}" required>
                        <div class="invalid-feedback">Email không hợp lệ.</div>
                    </div>
                    <button type="submit" class="btn btn-primary w-100">Gửi OTP</button>
                </form>
                <div class="mt-3 text-center small">
                    <a href="${pageContext.request.contextPath}/login">Quay lại đăng nhập</a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
