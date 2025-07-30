<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.pahanaedu.model.User" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    } else if (!"Admin".equalsIgnoreCase(user.getRole())) {
        response.sendRedirect("index.jsp"); // Redirect non-admin users
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Add User</title>

  <!-- Fonts and Icons -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" />
  <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" />
     <link rel="icon" type="image/x-icon" href="images/favicon.png"> 
  <!-- Styles -->
  <link rel="stylesheet" href="css/user-management.css" />
  <link rel="stylesheet" href="css/sidebar-header.css" />
  <link rel="stylesheet" href="alert/dist/sweetalert.css" />
</head>
<body>

<input type="hidden" id="status" value="<%= request.getAttribute("status") != null ? request.getAttribute("status") : "" %>" />

<div class="container">
  <%@ include file="sidebar.jsp" %>
  <%@ include file="header.jsp" %>

  <div class="main-content">
    <div class="page-title">
      <div class="title">Add New User</div>
      <div class="action-buttons">
        <a href="usermanagement.jsp" class="btn btn-outline">
          <i class="fas fa-arrow-left"></i> Back
        </a>
      </div>
    </div>

    <!-- User Form -->
    <div class="table-card">
      <div class="card-title">
        <h3><i class="fas fa-user-plus"></i> User Details</h3>
      </div>

      <div style="padding: 24px;">
        <form action="adduser" method="post" enctype="multipart/form-data">
          <div class="form-row-two">
            <div class="form-group">
              <label for="name">Full Name <span style="color:red">*</span></label>
              <input type="text" name="name" id="name" class="form-input" placeholder="Enter full name" required autocomplete="name" />
            </div>
            <div class="form-group">
              <label for="email">Email <span style="color:red">*</span></label>
              <input type="email" name="email" id="email" class="form-input" placeholder="Enter email" required autocomplete="email" />
            </div>
          </div>

          <div class="form-row-two">
            <div class="form-group">
              <label for="pass">Password <span style="color:red">*</span></label>
              <input type="password" name="pass" id="pass" class="form-input" required pattern=".{6,}" title="Password must be at least 6 characters" autocomplete="new-password" />
            </div>
            <div class="form-group">
              <label for="contact">Contact Number <span style="color:red">*</span></label>
              <input type="text" name="contact" id="contact" class="form-input" placeholder="Enter contact number" pattern="^\d{10,15}$" required />
            </div>
          </div>

          <div class="form-row-two">
            <div class="form-group">
              <label for="role">Role <span style="color:red">*</span></label>
              <select name="role" id="role" class="form-input" required>
                <option value="">Select Role</option>
                <option value="Admin">Admin</option>
                <option value="Employee">Employee</option>
              </select>
            </div>
            <div class="form-group">
              <label for="photo">Profile Picture <span style="color:red">*</span></label>
              <input type="file" name="photo" id="photo" class="form-input" accept="image/*" required />
            </div>
          </div>

          <div class="form-row" style="margin-top: 30px;">
            <button type="submit" class="btn btn-primary">
              <i class="fas fa-save"></i> Save User
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>

<!-- JS -->
<script src="vendor/jquery/jquery.min.js"></script>
<script src="js/main.js"></script>
<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>

<script>
  const status = document.getElementById("status").value;
  if (status === "success") {
    swal("Success", "User added successfully!", "success");
  } else if (status === "failed") {
    swal("Error", "Something went wrong. Please try again.", "error");
  } else if (status === "email_exists") {
    swal("Warning", "Email already exists. Please use a different email.", "warning");
  }
</script>

</body>
</html>
