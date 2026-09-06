<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>${product.name}</title>
</head>
<body>
<nav aria-label="breadcrumb">
    <ol class="breadcrumb">
        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/waiting">Trang chủ</a></li>
        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/product">Sản phẩm</a></li>
        <li class="breadcrumb-item active">${product.name}</li>
    </ol>
</nav>

<div class="row">
    <div class="col-md-5">
        <c:choose>
            <c:when test="${not empty product.image}">
                <img src="${pageContext.request.contextPath}/image?fname=${product.image}"
                     class="img-fluid rounded shadow-sm" alt="${product.name}">
            </c:when>
            <c:otherwise>
                <div class="bg-light rounded d-flex align-items-center justify-content-center"
                     style="height:300px;">No image</div>
            </c:otherwise>
        </c:choose>
    </div>
    <div class="col-md-7">
        <h2>${product.name}</h2>
        <p class="text-danger fs-4 fw-bold">${product.price} ₫</p>
        <p><span class="badge bg-secondary">${product.category.name}</span>
           <span class="badge ${product.status == 1 ? 'bg-success' : 'bg-danger'}">
               ${product.status == 1 ? 'Đang bán' : 'Ngừng bán'}
           </span>
        </p>
        <p>Số lượng: <strong>${product.quantity}</strong></p>
        <hr>
        <h5>Mô tả</h5>
        <p>${product.description != null ? product.description : 'Không có mô tả.'}</p>
        <a href="${pageContext.request.contextPath}/product" class="btn btn-outline-primary">← Danh sách</a>
    </div>
</div>
</body>
</html>
