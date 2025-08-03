<%@ page import="java.util.List, java.util.ArrayList" %>
<%@ page import="com.pahanaedu.model.User" %>
<%@ page import="com.pahanaedu.dao.UserDao" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    User loggedUser = (User) session.getAttribute("user");
    if (loggedUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    String role = loggedUser.getRole();
    boolean isAdminOrManager = "Admin".equalsIgnoreCase(role) || "Manager".equalsIgnoreCase(role);

    String filterEmpId = request.getParameter("uempid");
    String filterRole = request.getParameter("role");

    List<User> users = UserDao.getAllUsers();

    if ((filterEmpId != null && !filterEmpId.trim().isEmpty() && !"PEB-EMP-".equals(filterEmpId.trim())) ||
        (filterRole != null && !filterRole.trim().isEmpty())) {

        List<User> filtered = new ArrayList<>();
        for (User u : users) {
            boolean empMatch = (filterEmpId == null || filterEmpId.trim().isEmpty() || "PEB-EMP-".equals(filterEmpId.trim())) ||
                               (u.getUempid() != null && u.getUempid().toLowerCase().contains(filterEmpId.trim().toLowerCase()));

            boolean roleMatch = (filterRole == null || filterRole.trim().isEmpty()) ||
                                (u.getRole() != null && u.getRole().equalsIgnoreCase(filterRole.trim()));

            if (empMatch && roleMatch) {
                filtered.add(u);
            }
        }
        users = filtered;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>User Management</title>
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" />
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css" />
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap" rel="stylesheet" />
  <link rel="stylesheet" href="css/sidebar-header.css" />
  <link rel="stylesheet" href="css/user-management.css" />
  <link rel="icon" type="image/x-icon" href="images/favicon.png">
</head>
<body>

<div class="container">
  <%@ include file="sidebar.jsp" %>
  <%@ include file="header.jsp" %>

  <div class="main-content">
    <div class="page-title">
      <div class="title">User Management</div>
      <div class="action-buttons">
        <% if (isAdminOrManager) { %>
          <a href="adduser.jsp" class="btn btn-primary">
            <i class="fas fa-user-plus"></i> Add User
          </a>
        <% } %>
      </div>
    </div>

    <!-- Filter Form -->
    <form method="get" class="form-row-two" style="margin-bottom: 20px;">
      <div class="form-group">
        <label for="uempid">Filter by Employee ID:</label>
        <input type="text" name="uempid" id="empInput" class="form-input"
               value="<%= (filterEmpId != null) ? filterEmpId : "PEB-EMP-" %>"
               placeholder="PEB-EMP-XXX">
      </div>
      <div class="form-group">
        <label for="role">Filter by Role:</label>
        <select name="role" id="role" class="form-input">
          <option value="">All Roles</option>
          <option value="Admin" <%= "Admin".equalsIgnoreCase(filterRole) ? "selected" : "" %>>Admin</option>
          <option value="Manager" <%= "Manager".equalsIgnoreCase(filterRole) ? "selected" : "" %>>Manager</option>
          <option value="Cashier" <%= "Cashier".equalsIgnoreCase(filterRole) ? "selected" : "" %>>Cashier</option>
        </select>
      </div>
      <div style="display: flex; align-items: flex-end; gap: 10px;">
        <button type="submit" class="btn btn-primary"><i class="fas fa-filter"></i> Filter</button>
        <a href="usermanagement.jsp" class="btn btn-outline"><i class="fas fa-times"></i> Clear</a>
      </div>
    </form>

    <div class="table-card">
      <div class="card-title">
        <h3><i class="fas fa-users"></i> Existing Users</h3>
      </div>

      <table class="data-table">
        <thead>
          <tr>
            <th>Photo</th>
            <th>Employee ID</th>
            <th>Name</th>
            <th>Email</th>
            <th>Phone</th>
            <th>Role</th>
            <th>Status</th>
            <% if (isAdminOrManager) { %>
              <th>Actions</th>
            <% } %>
          </tr>
        </thead>
        <tbody>
          <% if (users == null || users.isEmpty()) { %>
            <tr>
              <td colspan="<%= isAdminOrManager ? 8 : 7 %>" style="text-align:center; color:red;">No records found.</td>
            </tr>
          <% } else {
               for (User user : users) {
          %>
            <tr>
              <td>
                <img src="userImage?email=<%= user.getEmail() %>"
                     alt="User Photo"
                     style="width:40px; height:40px; border-radius:50%; object-fit:cover;"
                     onerror="this.onerror=null;this.src='images/default-user.png';" />
              </td>
              <td><%= user.getUempid() != null ? user.getUempid() : "N/A" %></td>
              <td><%= user.getName() %></td>
              <td><%= user.getEmail() %></td>
              <td><%= user.getMobile() %></td>
              <td><%= user.getRole() %></td>
              <td>
                <span class="status active">
                  <i class="fas fa-check-circle"></i> Active
                </span>
              </td>
              <% if (isAdminOrManager) { %>
                <td>
                  <form action="edituser.jsp" method="get" style="display:inline;">
                    <input type="hidden" name="email" value="<%= user.getEmail() %>" />
                    <button type="submit" class="btn btn-outline btn-sm">
                      <i class="fas fa-edit"></i> Edit
                    </button>
                  </form>
                  <form action="deleteUser" method="post" class="delete-form" style="display:inline;">
                    <input type="hidden" name="uempid" value="<%= user.getUempid() %>" />
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

<script src="vendor/jquery/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<script>
  // Protect PEB-EMP- prefix
  const empPrefix = "PEB-EMP-";
  const empInput = document.getElementById("empInput");

  window.addEventListener("DOMContentLoaded", () => {
    if (!empInput.value.startsWith(empPrefix)) {
      empInput.value = empPrefix;
      empInput.setSelectionRange(empPrefix.length, empPrefix.length);
    }
  });

  empInput.addEventListener("keydown", (e) => {
    const cursor = empInput.selectionStart;
    if ((e.key === "Backspace" || e.key === "ArrowLeft") && cursor <= empPrefix.length) {
      e.preventDefault();
    }
    if (cursor < empPrefix.length && e.key.length === 1) {
      e.preventDefault();
    }
  });

  empInput.addEventListener("input", () => {
    if (!empInput.value.startsWith(empPrefix)) {
      const digits = empInput.value.replace(/[^0-9]/g, "").slice(0, 3);
      empInput.value = empPrefix + digits;
    }
  });

  const urlParams = new URLSearchParams(window.location.search);
  const status = urlParams.get("status");

  if (status === "success") {
    Swal.fire("Success!", "User updated successfully!", "success");
  } else if (status === "delete_success") {
    Swal.fire("Deleted!", "User deleted successfully!", "success");
  } else if (status === "failed") {
    Swal.fire("Error", "Operation failed.", "error");
  } else if (status === "invalid") {
    Swal.fire("Warning", "Invalid user data.", "warning");
  }

  document.querySelectorAll(".delete-btn").forEach(function (btn) {
    btn.addEventListener("click", function () {
      const form = btn.closest(".delete-form");
      Swal.fire({
        title: "Are you sure?",
        text: "This action cannot be undone.",
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
</script>

<script src="js/main.js"></script>
</body>
</html>

