<%@ page import="com.pahanaedu.dao.CustomerDao" %>
<%@ page import="com.pahanaedu.model.Customer" %>
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

    String account = request.getParameter("account");
    if (account == null || account.isEmpty()) {
        response.sendRedirect("customermanagement.jsp?status=failed");
        return;
    }

    Customer customer = CustomerDao.getCustomerByAccount(account);
    if (customer == null) {
        response.sendRedirect("customermanagement.jsp?status=not_found");
        return;
    }

    String status = request.getParameter("status") != null ? request.getParameter("status") : "";
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Edit Customer</title>
  <link rel="stylesheet" href="css/product-management.css">
  <link rel="stylesheet" href="css/sidebar-header.css">
  <link rel="stylesheet" href="alert/dist/sweetalert.css">
  <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
</head>
<body>

<div class="container">
  <%@ include file="sidebar.jsp" %>
  <%@ include file="header.jsp" %>

  <div class="main-content">
    <div class="page-title">
      <div class="title">Edit Customer</div>
      <div class="action-buttons">
        <a href="customermanagement.jsp" class="btn btn-outline"><i class="fas fa-arrow-left"></i> Back</a>
      </div>
    </div>

    <div class="table-card">
      <div class="card-title">
        <h3><i class="fas fa-user-edit"></i> Customer Details</h3>
      </div>

      <div style="padding: 24px;">
        <form action="UpdateCustomer" method="post" enctype="multipart/form-data">
          <input type="hidden" name="account_number" value="<%= customer.getAccountNumber() %>" />

          <div class="form-row-two">
            <div class="form-group">
              <label>Full Name *</label>
              <input type="text" name="full_name" class="form-input" required value="<%= customer.getFullName() %>">
            </div>
            <div class="form-group">
              <label>Email</label>
              <input type="email" name="email" class="form-input" value="<%= customer.getEmail() != null ? customer.getEmail() : "" %>">
            </div>
          </div>

          <div class="form-row-two">
            <div class="form-group">
              <label>Phone</label>
              <input type="text" name="phone" class="form-input" value="<%= customer.getPhone() != null ? customer.getPhone() : "" %>">
            </div>
            <div class="form-group">
              <label>City</label>
              <input type="text" name="city" class="form-input" value="<%= customer.getCity() != null ? customer.getCity() : "" %>">
            </div>
          </div>

          <div class="form-row-two">
            <div class="form-group" style="flex: 1;">
              <label>Address</label>
              <textarea name="address" class="form-input" rows="3"><%= customer.getAddress() != null ? customer.getAddress() : "" %></textarea>
            </div>
            <div class="form-group">
              <label>Postal Code</label>
              <input type="text" name="postal_code" class="form-input" value="<%= customer.getPostalCode() != null ? customer.getPostalCode() : "" %>">
            </div>
          </div>

          <div class="form-group">
            <label>Customer Image</label>
            <input type="file" name="photo" class="form-input" accept="image/*">
            <small>Leave empty to keep current image</small>
          </div>

          <div class="form-row" style="margin-top: 30px;">
            <button type="submit" class="btn btn-primary">
              <i class="fas fa-save"></i> Update Customer
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>

<script>
  const status = "<%= status %>";
  
  if (status === "update_success") {
      swal("Success", "Customer updated successfully!", "success");
  } else if (status === "duplicate_email") {
      swal("Warning", "Email already exists for another customer!", "warning");
  } else if (status === "failed") {
      swal("Error", "Failed to update customer. Please try again.", "error");
  } else if (status === "error") {
      swal("Error", "Unexpected error occurred.", "error");
  } else if (status === "not_found") {
      swal("Error", "Customer not found.", "error");
  }
</script>

</body>
</html>
