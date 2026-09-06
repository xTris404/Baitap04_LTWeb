<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Kích hoạt tài khoản</title>
</head>
<body>
<div class="row justify-content-center">
    <div class="col-md-5">
        <div class="card shadow-sm">
            <div class="card-body p-4">
                <h3 class="card-title text-center mb-4">Kích hoạt tài khoản (OTP)</h3>
                <c:if test="${not empty alert}">
                    <div class="alert alert-danger">${alert}</div>
                </c:if>
                <c:if test="${not empty success}">
                    <div class="alert alert-success">${success}</div>
                </c:if>
                <form class="needs-validation" novalidate method="post"
                      action="${pageContext.request.contextPath}/activate">
                    <div class="mb-3">
                        <label class="form-label">Email</label>
                        <input type="email" name="email" class="form-control" value="${email}" required>
                        <div class="invalid-feedback">Email không hợp lệ.</div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Mã OTP (6 số)</label>
                        <input type="text" name="otp" class="form-control" required pattern="[0-9]{6}" maxlength="6">
                        <div class="invalid-feedback">OTP phải là 6 chữ số.</div>
                    </div>
                    <button type="submit" class="btn btn-primary w-100">Kích hoạt</button>
                </form>
                <form method="post" action="${pageContext.request.contextPath}/resend-otp" class="mt-2">
                    <input type="hidden" name="email" value="${email}">
                    <button type="submit" class="btn btn-outline-secondary w-100 btn-sm">Gửi lại OTP</button>
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
