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
  <title>Add Customer</title>

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
      <div class="title">Add New Customer</div>
      <div class="action-buttons">
        <a href="customermanagement.jsp" class="btn btn-outline">
          <i class="fas fa-arrow-left"></i> Back
        </a>
      </div>
    </div>

    <!-- Customer Form -->
    <div class="table-card">
      <div class="card-title">
        <h3><i class="fas fa-user-plus"></i> Customer Details</h3>
      </div>

      <div style="padding: 24px;">
        <form action="AddCustomer" method="post" enctype="multipart/form-data">
          <div class="form-row-two">
            <div class="form-group">
              <label for="account_number">Account Number <span style="color:red">*</span></label>
              <input
                type="text"
                id="account_number"
                name="account_number"
                class="form-input"
                maxlength="20"
                required
                pattern="PEB-ACC-\d{3,}"
                title="Enter at least 3 digits after PEB-ACC- (e.g., PEB-ACC-001)"
                autocomplete="off"
              />
            </div>
            <div class="form-group">
              <label for="full_name">Full Name <span style="color:red">*</span></label>
              <input type="text" id="full_name" name="full_name" class="form-input" required />
            </div>
          </div>

          <div class="form-row-two">
            <div class="form-group">
              <label for="email">Email</label>
              <input type="email" id="email" name="email" class="form-input" />
            </div>
            <div class="form-group">
              <label for="phone">Phone</label>
              <input type="text" id="phone" name="phone" class="form-input" />
            </div>
          </div>

          <div class="form-row-two">
            <div class="form-group">
              <label for="address">Address</label>
              <textarea id="address" name="address" class="form-input" rows="3"></textarea>
            </div>
            <div class="form-group">
              <label for="city">City</label>
              <input type="text" id="city" name="city" class="form-input" />
            </div>
          </div>

          <div class="form-row-two">
            <div class="form-group">
              <label for="postal_code">Postal Code</label>
              <input type="text" id="postal_code" name="postal_code" class="form-input" />
            </div>
            <div class="form-group">
              <label for="photo">Customer Image <span style="color:red">*</span></label>
              <input type="file" id="photo" name="photo" class="form-input" accept="image/*" required />
            </div>
          </div>

          <div class="form-row" style="margin-top: 30px;">
            <button type="submit" class="btn btn-primary">
              <i class="fas fa-plus-circle"></i> Add Customer
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
    swal("Success", "Customer added successfully!", "success");
  } else if (status === "duplicate_account") {
    swal("Warning", "Account number already exists.", "warning");
  } else if (status === "duplicate_email") {
    swal("Warning", "Email address already exists.", "warning");
  } else if (status === "no_image") {
    swal("Warning", "Please upload a customer photo.", "warning");
  } else if (status === "failed") {
    swal("Error", "Failed to add customer. Please try again.", "error");
  }

  const prefix = "PEB-ACC-";
  const accountInput = document.getElementById("account_number");

  window.addEventListener("DOMContentLoaded", () => {
    if (!accountInput.value.startsWith(prefix)) {
      accountInput.value = prefix;
      accountInput.setSelectionRange(prefix.length, prefix.length);
    }
  });

  accountInput.addEventListener("keydown", (e) => {
    const cursor = accountInput.selectionStart;
    if ((e.key === "Backspace" || e.key === "ArrowLeft") && cursor <= prefix.length) {
      e.preventDefault();
    }
    if (cursor < prefix.length && e.key.length === 1) {
      e.preventDefault();
    }
  });

  accountInput.addEventListener("input", () => {
    if (!accountInput.value.startsWith(prefix)) {
      const digits = accountInput.value.replace(/[^0-9]/g, "").slice(0, 3);
      accountInput.value = prefix + digits;
    }
  });
</script>

</body>
</html>


