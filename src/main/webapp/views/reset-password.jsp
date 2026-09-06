<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đặt lại mật khẩu</title>
</head>
<body>
<div class="row justify-content-center">
    <div class="col-md-5">
        <div class="card shadow-sm">
            <div class="card-body p-4">
                <h3 class="card-title text-center mb-4">Đặt lại mật khẩu</h3>
                <c:if test="${not empty alert}">
                    <div class="alert alert-danger">${alert}</div>
                </c:if>
                <c:if test="${not empty success}">
                    <div class="alert alert-success">${success}</div>
                </c:if>
                <form class="needs-validation" novalidate method="post"
                      action="${pageContext.request.contextPath}/reset-password">
                    <div class="mb-3">
                        <label class="form-label">Email</label>
                        <input type="email" name="email" class="form-control" value="${email}" required>
                        <div class="invalid-feedback">Email không hợp lệ.</div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Mã OTP</label>
                        <input type="text" name="otp" class="form-control" required pattern="[0-9]{6}" maxlength="6">
                        <div class="invalid-feedback">OTP 6 chữ số.</div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Mật khẩu mới</label>
                        <input type="password" name="password" class="form-control" required minlength="6">
                        <div class="invalid-feedback">Tối thiểu 6 ký tự.</div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Xác nhận mật khẩu</label>
                        <input type="password" name="confirm" class="form-control" required minlength="6">
                        <div class="invalid-feedback">Mật khẩu xác nhận không khớp.</div>
                    </div>
                    <button type="submit" class="btn btn-primary w-100">Đặt lại mật khẩu</button>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
