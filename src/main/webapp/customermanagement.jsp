<%@ page import="java.util.List, java.util.ArrayList" %>
<%@ page import="com.pahanaedu.model.User" %>
<%@ page import="com.pahanaedu.model.Customer" %>
<%@ page import="com.pahanaedu.dao.CustomerDao" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    User loggedUser = (User) session.getAttribute("user");
    if (loggedUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    boolean isAdmin = "Admin".equalsIgnoreCase(loggedUser.getRole());

    String filterAcc = request.getParameter("account");
    String filterEmail = request.getParameter("email");

    List<Customer> customers = CustomerDao.getAllCustomers();

    if ((filterAcc != null && !filterAcc.trim().isEmpty() && !"PEB-ACC-".equals(filterAcc.trim())) ||
        (filterEmail != null && !filterEmail.trim().isEmpty())) {

        List<Customer> filtered = new ArrayList<>();
        for (Customer c : customers) {
            boolean accMatch = (filterAcc == null || filterAcc.trim().isEmpty() || "PEB-ACC-".equals(filterAcc.trim())) ||
                               (c.getAccountNumber() != null && c.getAccountNumber().toLowerCase().contains(filterAcc.trim().toLowerCase()));

            boolean emailMatch = (filterEmail == null || filterEmail.trim().isEmpty()) ||
                                 (c.getEmail() != null && c.getEmail().toLowerCase().contains(filterEmail.trim().toLowerCase()));

            if (accMatch && emailMatch) {
                filtered.add(c);
            }
        }
        customers = filtered;
    }
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

    <!-- Filter Form -->
    <form method="get" class="form-row-two" style="margin-bottom: 20px;" onsubmit="return true;">
      <div class="form-group">
        <label for="account">Filter by Account Number:</label>
        <input type="text" name="account" id="accountInput" class="form-input"
               value="<%= (filterAcc != null) ? filterAcc : "PEB-ACC-" %>"
               placeholder="PEB-ACC-XXX">
      </div>
      <div class="form-group">
        <label for="email">Filter by Email:</label>
        <input type="text" name="email" class="form-input"
               value="<%= (filterEmail != null) ? filterEmail : "" %>"
               placeholder="example@email.com">
      </div>
      <div style="display: flex; align-items: flex-end; gap: 10px;">
        <button type="submit" class="btn btn-primary"><i class="fas fa-filter"></i> Filter</button>
        <a href="customermanagement.jsp" class="btn btn-outline"><i class="fas fa-times"></i> Clear</a>
      </div>
    </form>

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
            <th>Address</th>
            <th>Registered</th>
            <% if (isAdmin) { %>
              <th>Actions</th>
            <% } %>
          </tr>
        </thead>
        <tbody>
          <% if (customers == null || customers.isEmpty()) { %>
            <tr>
              <td colspan="<%= isAdmin ? 9 : 8 %>" style="text-align: center; color: red;">No records found.</td>
            </tr>
          <% } else {
              for (Customer c : customers) {
          %>
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
              <td style="max-width: 200px; white-space: normal;">
                <div><%= c.getAddress() != null ? c.getAddress() : "-" %></div>
                <div style="font-size: 0.85em; color: #666;">
                  <%= c.getPostalCode() != null ? c.getPostalCode() : "" %>
                </div>
              </td>
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
          <% } } %>
        </tbody>
      </table>
    </div>
  </div>
</div>

<!-- Scripts -->
<script src="vendor/jquery/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<script>
  // Protect "PEB-ACC-" prefix
  const prefix = "PEB-ACC-";
  const input = document.getElementById("accountInput");

  window.addEventListener("DOMContentLoaded", () => {
    if (!input.value.startsWith(prefix)) {
      input.value = prefix;
      input.setSelectionRange(prefix.length, prefix.length);
    }
  });

  input.addEventListener("keydown", function (e) {
    const cursor = input.selectionStart;
    if ((e.key === "Backspace" || e.key === "ArrowLeft") && cursor <= prefix.length) {
      e.preventDefault();
    }
    if (cursor < prefix.length && e.key.length === 1) {
      e.preventDefault();
    }
  });

  input.addEventListener("input", function () {
    if (!input.value.startsWith(prefix)) {
      const digits = input.value.replace(/[^0-9]/g, "").slice(0, 3);
      input.value = prefix + digits;
    }
  });

  // Delete confirm
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

  // Status messages
  const status = new URLSearchParams(window.location.search).get("status");
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

