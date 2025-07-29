<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.pahanaedu.model.User" %>

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
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Add Product</title>

  <!-- Fonts and Icons -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" />
  <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" />
  <link rel="icon" type="image/x-icon" href="images/favicon.png">

  <!-- Styles -->
  <link rel="stylesheet" href="css/product-management.css" />
  <link rel="stylesheet" href="css/sidebar-header.css" />
  <link rel="stylesheet" href="alert/dist/sweetalert.css" />
</head>
<body>

<input type="hidden" id="status" value="<%= request.getParameter("status") != null ? request.getParameter("status") : "" %>" />

<div class="container">
  <%@ include file="sidebar.jsp" %>
  <%@ include file="header.jsp" %>

  <div class="main-content">
    <div class="page-title">
      <div class="title">Add New Product</div>
      <div class="action-buttons">
        <a href="productmanagement.jsp" class="btn btn-outline">
          <i class="fas fa-arrow-left"></i> Back
        </a>
      </div>
    </div>

    <!-- Product Form -->
    <div class="table-card">
      <div class="card-title">
        <h3><i class="fas fa-box-open"></i> Product Details</h3>
      </div>

      <div style="padding: 24px;">
        <form action="AddProduct" method="post" enctype="multipart/form-data">
          <div class="form-row-two">
            <div class="form-group">
              <label for="itemid">Item ID <span style="color:red">*</span></label>
              <input
                type="text"
                id="itemid"
                name="itemid"
                class="form-input"
                maxlength="20"
                required
                pattern="PEB-ITEM-\d{3,}"
                title="Enter 3 digits only (e.g., 001)"
                autocomplete="off"
              />
            </div>
            <div class="form-group">
              <label for="name">Product Name <span style="color:red">*</span></label>
              <input type="text" id="name" name="name" class="form-input" required />
            </div>
          </div>

          <div class="form-row-two">
            <div class="form-group">
              <label for="price">Price ($) <span style="color:red">*</span></label>
              <input type="number" id="price" name="price" class="form-input" step="0.01" min="0" placeholder="0.00" required />
            </div>
            <div class="form-group">
              <label for="quantity">Quantity <span style="color:red">*</span></label>
              <input type="number" id="quantity" name="quantity" class="form-input" min="0" placeholder="0" required />
            </div>
          </div>

          <div class="form-row-two">
            <div class="form-group" style="flex: 1;">
              <label for="description">Description</label>
              <textarea id="description" name="description" class="form-input" rows="4" placeholder="Enter product description"></textarea>
            </div>
            <div class="form-group">
              <label for="photo">Product Image <span style="color:red">*</span></label>
              <input type="file" id="photo" name="photo" class="form-input" accept="image/*" required />
            </div>
          </div>

          <div class="form-row" style="margin-top: 30px;">
            <button type="submit" class="btn btn-primary">
              <i class="fas fa-plus-circle"></i> Add Product
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>

<!-- JS -->
<script src="vendor/jquery/jquery.min.js"></script>
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>

<script>
  const status = document.getElementById("status").value;
  if (status === "success") {
    swal("Success", "Product added successfully!", "success");
  } else if (status === "invalid_input") {
    swal("Error", "Invalid price or quantity entered.", "error");
  } else if (status === "no_image") {
    swal("Warning", "Please upload a product image.", "warning");
  } else if (status === "duplicate") {
    swal("Warning", "Item ID already exists. Please use a different one.", "warning");
  } else if (status === "failed") {
    swal("Error", "Failed to add product. Please try again.", "error");
  }

  const prefix = "PEB-ITEM-";
  const itemInput = document.getElementById("itemid");

  window.addEventListener("DOMContentLoaded", () => {
    if (!itemInput.value.startsWith(prefix)) {
      itemInput.value = prefix;
      itemInput.setSelectionRange(prefix.length, prefix.length);
    }
  });

  itemInput.addEventListener("keydown", (e) => {
    const cursor = itemInput.selectionStart;
    if ((e.key === "Backspace" || e.key === "ArrowLeft") && cursor <= prefix.length) {
      e.preventDefault();
    }
    if (cursor < prefix.length && e.key.length === 1) {
      e.preventDefault();
    }
  });

  itemInput.addEventListener("input", () => {
    if (!itemInput.value.startsWith(prefix)) {
      const digits = itemInput.value.replace(/[^0-9]/g, "").slice(0, 3);
      itemInput.value = prefix + digits;
    }
  });
</script>

</body>
</html>


