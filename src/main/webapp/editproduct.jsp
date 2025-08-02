<%@ page import="com.pahanaedu.dao.ProductDao" %>
<%@ page import="com.pahanaedu.model.Product" %>
<%@ page import="com.pahanaedu.model.User" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    User loggedUser = (User) session.getAttribute("user");
    if (loggedUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    if (!"Admin".equalsIgnoreCase(loggedUser.getRole())) {
        response.sendRedirect("index.jsp");
        return;
    }

    String idParam = request.getParameter("id");
    if (idParam == null || idParam.isEmpty()) {
        response.sendRedirect("productmanagement.jsp?status=failed");
        return;
    }

    int productId = Integer.parseInt(idParam);
    Product product = ProductDao.getProductById(productId);

    if (product == null) {
        response.sendRedirect("productmanagement.jsp?status=not_found");
        return;
    }

    // You can hardcode categories here or fetch from DB if you want
    String[] categories = {"Fiction", "Non-Fiction", "Science", "Biography", "Children", "Other"};
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Edit Product</title>
  <link rel="stylesheet" href="css/product-management.css" />
  <link rel="stylesheet" href="css/sidebar-header.css" />
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" />
</head>
<body>

<div class="container">
  <%@ include file="sidebar.jsp" %>
  <%@ include file="header.jsp" %>

  <div class="main-content">
    <div class="page-title">
      <div class="title">Edit Product</div>
      <div class="action-buttons">
        <a href="productmanagement.jsp" class="btn btn-outline">
          <i class="fas fa-arrow-left"></i> Back
        </a>
      </div>
    </div>

    <div class="table-card">
      <div class="card-title">
        <h3><i class="fas fa-box-open"></i> Product Details</h3>
      </div>

      <div style="padding: 24px;">
        <form action="<%=request.getContextPath()%>/UpdateProduct" method="post" enctype="multipart/form-data">
          <input type="hidden" name="id" value="<%= product.getId() %>" />

          <div class="form-row-two">
            <div class="form-group">
              <label for="itemid">Item ID *</label>
              <input type="text" name="itemid" class="form-input" required value="<%= product.getItemId() %>" />
            </div>
            <div class="form-group">
              <label for="name">Product Name *</label>
              <input type="text" name="name" class="form-input" required value="<%= product.getName() %>" />
            </div>
          </div>

          <div class="form-row-two">
            <div class="form-group">
              <label for="price">Price ($) *</label>
              <input type="number" name="price" class="form-input" step="0.01" min="0" required value="<%= product.getPrice() %>" />
            </div>
            <div class="form-group">
              <label for="quantity">Quantity *</label>
              <input type="number" name="quantity" class="form-input" min="0" required value="<%= product.getQuantity() %>" />
            </div>
          </div>

          <div class="form-row-two">
            <div class="form-group">
              <label for="category">Category *</label>
              <select name="category" class="form-input" required>
                <option value="">-- Select Category --</option>
                <% for (String cat : categories) { %>
                  <option value="<%= cat %>" <%= cat.equals(product.getCategory()) ? "selected" : "" %>><%= cat %></option>
                <% } %>
              </select>
            </div>

            <div class="form-group" style="flex: 1;">
              <label for="description">Description</label>
              <textarea name="description" class="form-input" rows="4"><%= product.getDescription() %></textarea>
            </div>

            <div class="form-group">
              <label for="photo">Product Image</label>
              <input type="file" name="photo" class="form-input" accept="image/*" />
              <small>Leave empty to keep current image</small>
            </div>
          </div>

          <div class="form-row" style="margin-top: 30px;">
            <button type="submit" class="btn btn-primary">
              <i class="fas fa-save"></i> Update Product
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>

</body>
</html>


