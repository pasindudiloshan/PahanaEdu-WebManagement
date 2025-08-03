<%@ page import="java.util.List, java.util.ArrayList" %>
<%@ page import="com.pahanaedu.model.Product" %>
<%@ page import="com.pahanaedu.dao.ProductDao" %>
<%@ page import="com.pahanaedu.model.User" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    User loggedUser = (User) session.getAttribute("user");
    if (loggedUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    // Allow both Admin and Manager
    boolean isAdminOrManager = "Admin".equalsIgnoreCase(loggedUser.getRole()) || "Manager".equalsIgnoreCase(loggedUser.getRole());

    String filterName = request.getParameter("productName");
    String filterCategory = request.getParameter("category");

    List<Product> products = ProductDao.getAllProducts();

    if ((filterName != null && !filterName.trim().isEmpty()) ||
        (filterCategory != null && !filterCategory.trim().isEmpty())) {

        List<Product> filtered = new ArrayList<>();
        for (Product p : products) {
            boolean nameMatch = (filterName == null || filterName.trim().isEmpty()) ||
                                (p.getName() != null && p.getName().toLowerCase().contains(filterName.trim().toLowerCase()));

            boolean categoryMatch = (filterCategory == null || filterCategory.trim().isEmpty()) ||
                                    (p.getCategory() != null && p.getCategory().equalsIgnoreCase(filterCategory.trim()));

            if (nameMatch && categoryMatch) {
                filtered.add(p);
            }
        }
        products = filtered;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Product Management</title>

  <!-- Font Awesome & Google Fonts -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" />
  <link href="https://fonts.googleapis.com/css2?family=Poppins&display=swap" rel="stylesheet" />

  <!-- Your CSS -->
  <link rel="stylesheet" href="css/sidebar-header.css" />
  <link rel="stylesheet" href="css/product-management.css" />
  <link rel="icon" type="image/x-icon" href="images/favicon.png" />
</head>
<body>

<div class="container">
  <%@ include file="sidebar.jsp" %>
  <%@ include file="header.jsp" %>

  <div class="main-content">
    <div class="page-title">
      <div class="title">Product Management</div>
      <div class="action-buttons">
        <% if (isAdminOrManager) { %>
          <a href="addproduct.jsp" class="btn btn-primary">
            <i class="fas fa-plus"></i> Add Product
          </a>
        <% } %>
      </div>
    </div>

    <!-- Filter Form -->
    <form method="get" class="form-row-two" style="margin-bottom: 20px;" onsubmit="return validateProductFilterForm();">
      <div class="form-group">
        <label for="productName">Filter by Product Name:</label>
        <input type="text" name="productName" id="productName" class="form-input"
               value="<%= (filterName != null) ? filterName : "" %>"
               placeholder="Enter product name">
      </div>
      <div class="form-group">
        <label for="category">Filter by Category:</label>
        <select name="category" id="category" class="form-input">
          <option value="">All Categories</option>
          <option value="Novels" <%= "Novels".equals(filterCategory) ? "selected" : "" %>>Novels</option>
          <option value="Stationery" <%= "Stationery".equals(filterCategory) ? "selected" : "" %>>Stationery</option>
          <option value="Textbooks" <%= "Textbooks".equals(filterCategory) ? "selected" : "" %>>Textbooks</option>
          <option value="Magazines" <%= "Magazines".equals(filterCategory) ? "selected" : "" %>>Magazines</option>
          <option value="Comics" <%= "Comics".equals(filterCategory) ? "selected" : "" %>>Comics</option>
          <option value="Reference" <%= "Reference".equals(filterCategory) ? "selected" : "" %>>Reference</option>
          <option value="Others" <%= "Others".equals(filterCategory) ? "selected" : "" %>>Others</option>
        </select>
      </div>
      <div style="display: flex; align-items: flex-end; gap: 10px;">
        <button type="submit" class="btn btn-primary"><i class="fas fa-filter"></i> Filter</button>
        <a href="productmanagement.jsp" class="btn btn-outline"><i class="fas fa-times"></i> Clear</a>
      </div>
    </form>

    <div class="table-card">
      <div class="card-title">
        <h3><i class="fas fa-box"></i> Existing Products</h3>
      </div>

      <table class="data-table">
        <thead>
          <tr>
            <th>Cover</th>
            <th>Item ID</th>
            <th>Name</th>
            <th>Description</th>
            <th>Category</th>
            <th>Price (LKR)</th>
            <th>Quantity</th>
            <% if (isAdminOrManager) { %>
              <th>Actions</th>
            <% } %>
          </tr>
        </thead>
        <tbody>
          <% if (products == null || products.isEmpty()) { %>
            <tr>
              <td colspan="<%= isAdminOrManager ? 8 : 7 %>" style="text-align: center; color: red;">No records found.</td>
            </tr>
          <% } else {
              for (Product product : products) {
          %>
            <tr>
              <td>
                <img src="productImage?id=<%= product.getId() %>" 
                     alt="Cover" 
                     class="book-cover"
                     onerror="this.onerror=null;this.src='images/default-book.png';" />
              </td>
              <td><%= product.getItemId() %></td>
              <td><%= product.getName() %></td>
              <td><%= product.getDescription() %></td>
              <td><%= product.getCategory() %></td>
              <td><%= String.format("%.2f", product.getPrice()) %></td>
              <td><%= product.getQuantity() %></td>
              <% if (isAdminOrManager) { %>
                <td>
                  <a href="editproduct.jsp?id=<%= product.getId() %>" class="btn btn-outline btn-sm">
                    <i class="fas fa-edit"></i> Edit
                  </a>
                  <form action="deleteProduct" method="post" class="delete-form" style="display:inline;">
                    <input type="hidden" name="id" value="<%= product.getId() %>" />
                    <button type="button" class="btn btn-outline btn-sm delete-btn">
                      <i class="fas fa-trash-alt"></i>
                    </button>
                  </form>
                </td>
              <% } %>
            </tr>
          <% } } %>
        </tbody>
      </table>
    </div>
  </div>
</div>

<!-- JS -->
<script src="vendor/jquery/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<script>
  const status = new URLSearchParams(window.location.search).get("status");

  if (status === "success") {
    Swal.fire("Success!", "Product added successfully!", "success");
  } else if (status === "update_success") {
    Swal.fire("Updated!", "Product updated successfully!", "success");
  } else if (status === "delete_success") {
    Swal.fire("Deleted!", "Product deleted successfully!", "success");
  } else if (status === "failed") {
    Swal.fire("Error", "Operation failed.", "error");
  } else if (status === "not_found") {
    Swal.fire("Error", "Product not found.", "error");
  } else if (status === "error") {
    Swal.fire("Error", "An unexpected error occurred.", "error");
  }

  document.querySelectorAll(".delete-btn").forEach(function (btn) {
    btn.addEventListener("click", function () {
      const form = btn.closest(".delete-form");
      Swal.fire({
        title: "Are you sure?",
        text: "This will remove the product permanently.",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Yes, delete it!",
        cancelButtonText: "Cancel",
      }).then((result) => {
        if (result.isConfirmed) {
          form.submit();
        }
      });
    });
  });

  function validateProductFilterForm() {
    return true; // All filter combinations are allowed
  }
</script>

</body>
</html>

