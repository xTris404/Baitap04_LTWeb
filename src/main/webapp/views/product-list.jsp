<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Danh sách sản phẩm</title>
</head>
<body>
<h3 class="mb-3">Tất cả sản phẩm</h3>
<div class="row g-3">
    <c:choose>
        <c:when test="${empty products}">
            <div class="col-12"><div class="alert alert-info">Không có sản phẩm.</div></div>
        </c:when>
        <c:otherwise>
            <c:forEach items="${products}" var="p">
                <div class="col-6 col-md-4">
                    <div class="card h-100 shadow-sm">
                        <c:choose>
                            <c:when test="${not empty p.image}">
                                <img src="${pageContext.request.contextPath}/image?fname=${p.image}"
                                     class="card-img-top" alt="${p.name}"
                                     style="height:180px;object-fit:cover;">
                            </c:when>
                            <c:otherwise>
                                <div class="bg-light d-flex align-items-center justify-content-center"
                                     style="height:180px;">No image</div>
                            </c:otherwise>
                        </c:choose>
                        <div class="card-body">
                            <h5 class="card-title">
                                <a href="${pageContext.request.contextPath}/product/detail?id=${p.id}"
                                   class="text-decoration-none">${p.name}</a>
                            </h5>
                            <p class="text-danger fw-bold">${p.price} ₫</p>
                            <p class="small text-muted mb-0">${p.category.name} &middot; SL: ${p.quantity}</p>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</div>

<c:if test="${totalPages > 1}">
<nav class="mt-4">
    <ul class="pagination justify-content-center">
        <li class="page-item ${currentPage <= 1 ? 'disabled' : ''}">
            <a class="page-link" href="?page=${currentPage - 1}">Trước</a>
        </li>
        <c:forEach begin="1" end="${totalPages}" var="i">
            <li class="page-item ${i == currentPage ? 'active' : ''}">
                <a class="page-link" href="?page=${i}">${i}</a>
            </li>
        </c:forEach>
        <li class="page-item ${currentPage >= totalPages ? 'disabled' : ''}">
            <a class="page-link" href="?page=${currentPage + 1}">Sau</a>
        </li>
    </ul>
</nav>
</c:if>
</body>
</html>
