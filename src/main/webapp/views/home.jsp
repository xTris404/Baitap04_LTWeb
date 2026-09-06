<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Trang chủ</title>
</head>
<body>
<div class="mb-4">
    <h2>Xin chào, ${sessionScope.account.fullName != null ? sessionScope.account.fullName : sessionScope.account.userName}!</h2>
    <p class="text-muted">Bạn đã đăng nhập thành công.
        <c:if test="${not empty cookie.username}"> (Ghi nhớ qua Cookie)</c:if>
    </p>
</div>

<h4 class="mb-3">Sản phẩm mới nhất</h4>
<div class="row g-3">
    <c:choose>
        <c:when test="${empty latestProducts}">
            <div class="col-12">
                <div class="alert alert-info">Chưa có sản phẩm nào. Hãy thêm sản phẩm ở trang quản trị.</div>
            </div>
        </c:when>
        <c:otherwise>
            <c:forEach items="${latestProducts}" var="p">
                <div class="col-6 col-md-4 col-lg-3">
                    <div class="card h-100 shadow-sm">
                        <c:choose>
                            <c:when test="${not empty p.image}">
                                <img src="${pageContext.request.contextPath}/image?fname=${p.image}"
                                     class="card-img-top" alt="${p.name}"
                                     style="height:160px;object-fit:cover;">
                            </c:when>
                            <c:otherwise>
                                <div class="bg-light d-flex align-items-center justify-content-center"
                                     style="height:160px;">No image</div>
                            </c:otherwise>
                        </c:choose>
                        <div class="card-body">
                            <h6 class="card-title mb-1">
                                <a href="${pageContext.request.contextPath}/product/detail?id=${p.id}"
                                   class="text-decoration-none">${p.name}</a>
                            </h6>
                            <p class="card-text text-danger fw-bold mb-0">
                                ${p.price} ₫
                            </p>
                            <small class="text-muted">${p.category.name}</small>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</div>
<div class="mt-4">
    <a href="${pageContext.request.contextPath}/product" class="btn btn-outline-primary">Xem tất cả sản phẩm</a>
</div>
</body>
</html>
