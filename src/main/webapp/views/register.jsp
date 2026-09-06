<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Đăng ký</title>
</head>
<body>
<div class="row justify-content-center">
    <div class="col-md-6">
        <div class="card shadow-sm">
            <div class="card-body p-4">
                <h3 class="card-title text-center mb-4">Đăng ký tài khoản</h3>
                <c:if test="${not empty alert}">
                    <div class="alert alert-danger">${alert}</div>
                </c:if>
                <form class="needs-validation" novalidate method="post"
                      action="${pageContext.request.contextPath}/register">
                    <div class="mb-3">
                        <label class="form-label">Username *</label>
                        <input type="text" name="username" class="form-control" value="${username}"
                               required minlength="3" maxlength="50" pattern="[a-zA-Z0-9_]+">
                        <div class="invalid-feedback">Username 3-50 ký tự (chữ, số, _).</div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Email *</label>
                        <input type="email" name="email" class="form-control" value="${email}" required>
                        <div class="invalid-feedback">Email không hợp lệ.</div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Họ tên *</label>
                        <input type="text" name="fullname" class="form-control" value="${fullname}" required>
                        <div class="invalid-feedback">Họ tên không được để trống.</div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Số điện thoại</label>
                        <input type="text" name="phone" class="form-control" value="${phone}"
                               pattern="(0|\+84)[0-9]{9,10}">
                        <div class="invalid-feedback">SĐT dạng 0xxxxxxxxx hoặc +84xxxxxxxxx.</div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Mật khẩu *</label>
                        <input type="password" name="password" class="form-control" required minlength="6">
                        <div class="invalid-feedback">Tối thiểu 6 ký tự.</div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Xác nhận mật khẩu *</label>
                        <input type="password" name="confirm" class="form-control" required minlength="6">
                        <div class="invalid-feedback">Mật khẩu xác nhận không khớp.</div>
                    </div>
                    <button type="submit" class="btn btn-primary w-100">Đăng ký</button>
                </form>
                <div class="mt-3 text-center small">
                    Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
