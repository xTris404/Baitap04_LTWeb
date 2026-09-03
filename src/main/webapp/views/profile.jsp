<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Hồ sơ cá nhân</title>
</head>
<body>
<div class="page-center">
<div class="profile-card">
    <h2>Hồ sơ cá nhân</h2>

    <c:if test="${not empty success}">
        <h3 class="alert alert-success">${success}</h3>
    </c:if>
    <c:if test="${not empty error}">
        <h3 class="alert alert-danger">${error}</h3>
    </c:if>

    <div class="avatar-preview">
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
                <img src="${avatarUrl}" alt="avatar" id="avatarImg" class="avatar-img">
            </c:when>
            <c:otherwise>
                <div class="avatar-placeholder" id="avatarPlaceholder">
                    <c:choose>
                        <c:when test="${not empty user.fullName}">${fn:toUpperCase(fn:substring(user.fullName, 0, 1))}</c:when>
                        <c:otherwise>${fn:toUpperCase(fn:substring(user.userName, 0, 1))}</c:otherwise>
                    </c:choose>
                </div>
                <img src="" alt="avatar" id="avatarImg" class="avatar-img" style="display:none;">
            </c:otherwise>
        </c:choose>
    </div>

    <form action="<c:url value='/profile/update'/>" method="post" enctype="multipart/form-data">
        <section>
            <label>Tài khoản</label>
            <input type="text" class="form-control" value="${user.userName}" disabled>
        </section>

        <section>
            <label>Email</label>
            <input type="text" class="form-control" value="${user.email}" disabled>
        </section>

        <section>
            <label>Họ tên</label>
            <input type="text" name="fullname" class="form-control" value="${user.fullName}"
                   placeholder="Nhập họ tên" required maxlength="255">
        </section>

        <section>
            <label>Số điện thoại</label>
            <input type="text" name="phone" class="form-control" value="${user.phone}"
                   placeholder="VD: 0901234567" maxlength="20">
        </section>

        <section>
            <label>Ảnh đại diện</label>
            <input type="file" name="avatarFile" accept="image/png,image/jpeg,image/gif,image/webp"
                   onchange="previewAvatar(this)">
            <small>Bỏ trống nếu muốn giữ ảnh hiện tại &mdash; chấp nhận JPG/PNG/GIF/WEBP, tối đa 5MB</small>
        </section>

        <button type="submit" class="btn-submit">Lưu thay đổi</button>
    </form>
</div>
</div>

<script>
    function previewAvatar(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            reader.onload = function (e) {
                var img = document.getElementById('avatarImg');
                img.src = e.target.result;
                img.style.display = 'block';
                var placeholder = document.getElementById('avatarPlaceholder');
                if (placeholder) {
                    placeholder.style.display = 'none';
                }
            };
            reader.readAsDataURL(input.files[0]);
        }
    }
</script>
</body>
</html>
