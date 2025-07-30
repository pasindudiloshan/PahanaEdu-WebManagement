<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.pahanaedu.model.User" %>
<%@ page import="com.pahanaedu.model.Customer" %>
<%@ page import="com.pahanaedu.dao.CustomerDao" %>

<%
    User loggedUser = (User) session.getAttribute("user");
    if (loggedUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    boolean isAdmin = "Admin".equalsIgnoreCase(loggedUser.getRole());

    List<Customer> customers = CustomerDao.getAllCustomers();
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Customer Management</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <!-- Fonts and Icons -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" />
  <link rel="stylesheet" href="css/sidebar-header.css">
  <link rel="stylesheet" href="css/user-management.css">
  <link rel="icon" type="image/x-icon" href="images/favicon.png">
</head>
<body>

<div class="container">
  <%@ include file="sidebar.jsp" %>
  <%@ include file="header.jsp" %>

  <div class="main-content">
    <div class="page-title">
      <div class="title">Customer Management</div>
      <div class="action-buttons">
        <% if (isAdmin) { %>
          <a href="addcustomer.jsp" class="btn btn-primary">
            <i class="fas fa-user-plus"></i> Add Customer
          </a>
        <% } %>
      </div>
    </div>

    <div class="table-card">
      <div class="card-title">
        <h3><i class="fas fa-users"></i> Registered Customers</h3>
      </div>

      <table class="data-table">
        <thead>
          <tr>
            <th>Photo</th>
            <th>Account No</th>
            <th>Name</th>
            <th>Email</th>
            <th>Phone</th>
            <th>City</th>
            <th>Registered</th>
            <% if (isAdmin) { %>
              <th>Actions</th>
            <% } %>
          </tr>
        </thead>
        <tbody>
          <% for (Customer c : customers) { %>
            <tr>
              <td>
                <img src="customerImage?account=<%= c.getAccountNumber() %>"
                     alt="Customer Photo"
                     style="width:40px; height:40px; border-radius:50%; object-fit:cover;"
                     onerror="this.onerror=null;this.src='images/default-user.png';" />
              </td>
              <td><%= c.getAccountNumber() %></td>
              <td><%= c.getFullName() %></td>
              <td><%= c.getEmail() != null ? c.getEmail() : "-" %></td>
              <td><%= c.getPhone() != null ? c.getPhone() : "-" %></td>
              <td><%= c.getCity() != null ? c.getCity() : "-" %></td>
              <td><%= c.getRegistrationDate() %></td>
              
              <% if (isAdmin) { %>
                <td>
                  <form action="editcustomer.jsp" method="get" style="display:inline;">
                    <input type="hidden" name="account" value="<%= c.getAccountNumber() %>" />
                    <button type="submit" class="btn btn-outline btn-sm">
                      <i class="fas fa-edit"></i> Edit
                    </button>
                  </form>

                  <form action="DeleteCustomer" method="post" class="delete-form" style="display:inline;">
                    <input type="hidden" name="account" value="<%= c.getAccountNumber() %>" />
                    <button type="button" class="btn btn-outline btn-sm delete-btn">
                      <i class="fas fa-trash-alt"></i>
                    </button>
                  </form>
                </td>
              <% } %>
            </tr>
          <% } %>
        </tbody>
      </table>
    </div>
  </div>
</div>

<!-- Scripts -->
<script src="vendor/jquery/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<script>
  // Confirm delete
  document.querySelectorAll(".delete-btn").forEach(function (btn) {
    btn.addEventListener("click", function () {
      const form = btn.closest(".delete-form");

      Swal.fire({
        title: "Are you sure?",
        text: "This action cannot be undone.",
        icon: "warning",
        showCancelButton: true,
        confirmButtonText: "Yes, delete it!",
        cancelButtonText: "Cancel"
      }).then((result) => {
        if (result.isConfirmed) {
          form.submit();
        }
      });
    });
  });

  // Show status message
  const urlParams = new URLSearchParams(window.location.search);
  const status = urlParams.get("status");

  if (status === "success") {
    Swal.fire("Success!", "Operation completed successfully!", "success");
  } else if (status === "delete_success") {
    Swal.fire("Deleted!", "Customer deleted successfully!", "success");
  } else if (status === "failed") {
    Swal.fire("Error", "Operation failed.", "error");
  }
</script>

</body>
</html>
