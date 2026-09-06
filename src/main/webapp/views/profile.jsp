<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hồ sơ cá nhân</title>
</head>
<body>
<div class="row justify-content-center">
<div class="col-md-6">
<div class="card shadow-sm">
<div class="card-body p-4">
    <h3 class="card-title text-center mb-4">Hồ sơ cá nhân</h3>

    <c:if test="${not empty success}">
        <div class="alert alert-success">${success}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <div class="text-center mb-3">
        <c:choose>
            <c:when test="${not empty user.avatar}">
                <c:choose>
                    <c:when test="${fn:startsWith(user.avatar, 'http')}">
                        <c:url value="${user.avatar}" var="avatarUrl"/>
                    </c:when>
                    <c:otherwise>
                        <c:url value="/image?fname=${user.avatar}" var="avatarUrl"/>
                    </c:otherwise>
                </c:choose>
                <img src="${avatarUrl}" alt="avatar" id="avatarImg"
                     class="rounded-circle" style="width:100px;height:100px;object-fit:cover;">
            </c:when>
            <c:otherwise>
                <div class="rounded-circle bg-secondary text-white d-inline-flex align-items-center justify-content-center"
                     id="avatarPlaceholder" style="width:100px;height:100px;font-size:2rem;">
                    <c:choose>
                        <c:when test="${not empty user.fullName}">${fn:toUpperCase(fn:substring(user.fullName, 0, 1))}</c:when>
                        <c:otherwise>${fn:toUpperCase(fn:substring(user.userName, 0, 1))}</c:otherwise>
                    </c:choose>
                </div>
                <img src="" alt="avatar" id="avatarImg" class="rounded-circle"
                     style="width:100px;height:100px;object-fit:cover;display:none;">
            </c:otherwise>
        </c:choose>
    </div>

    <form class="needs-validation" novalidate method="post"
          action="<c:url value='/profile/update'/>" enctype="multipart/form-data">
        <div class="mb-3">
            <label class="form-label">Tài khoản</label>
            <input type="text" class="form-control" value="${user.userName}" disabled>
        </div>
        <div class="mb-3">
            <label class="form-label">Email</label>
            <input type="text" class="form-control" value="${user.email}" disabled>
        </div>
        <div class="mb-3">
            <label class="form-label">Họ tên *</label>
            <input type="text" name="fullname" class="form-control" value="${user.fullName}"
                   required maxlength="255">
            <div class="invalid-feedback">Họ tên bắt buộc.</div>
        </div>
        <div class="mb-3">
            <label class="form-label">Số điện thoại</label>
            <input type="text" name="phone" class="form-control" value="${user.phone}"
                   pattern="(0|\+84)[0-9]{9,10}" maxlength="20">
            <div class="invalid-feedback">SĐT dạng 0xxxxxxxxx hoặc +84xxxxxxxxx.</div>
        </div>
        <div class="mb-3">
            <label class="form-label">Ảnh đại diện</label>
            <input type="file" name="avatarFile" class="form-control"
                   accept="image/png,image/jpeg,image/gif,image/webp"
                   onchange="previewAvatar(this)">
            <div class="form-text">Bỏ trống nếu giữ ảnh hiện tại (JPG/PNG/GIF/WEBP, tối đa 5MB)</div>
        </div>
        <button type="submit" class="btn btn-primary w-100">Lưu thay đổi</button>
    </form>
</div>
</div>
</div>
</div>

<script>
    function previewAvatar(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            reader.onload = function (e) {
                var img = document.getElementById('avatarImg');
                img.src = e.target.result;
                img.style.display = 'inline-block';
                var placeholder = document.getElementById('avatarPlaceholder');
                if (placeholder) placeholder.style.display = 'none';
            };
            reader.readAsDataURL(input.files[0]);
        }
    }
</script>
</body>
</html>
