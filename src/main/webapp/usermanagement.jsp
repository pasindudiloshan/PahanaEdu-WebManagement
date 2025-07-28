<%@ page import="java.util.List" %>
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

    List<User> users = UserDao.getAllUsers();
    boolean isAdmin = "Admin".equalsIgnoreCase(role);
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
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
        <% if (isAdmin) { %>
          <a href="adduser.jsp" class="btn btn-primary">
            <i class="fas fa-user-plus"></i> Add User
          </a>
        <% } %>
      </div>
    </div>

    <div class="table-card">
      <div class="card-title">
        <h3><i class="fas fa-users"></i> Existing Users</h3>
      </div>

      <table class="data-table">
        <thead>
          <tr>
            <th>Photo</th>
            <th>Name</th>
            <th>Email</th>
            <th>Role</th>
            <th>Status</th>
            <% if (isAdmin) { %>
              <th>Actions</th>
            <% } %>
          </tr>
        </thead>
        <tbody>
          <% for (User user : users) { %>
            <tr>
              <td>
                <img src="userImage?email=<%= user.getEmail() %>" 
                     alt="User Photo" 
                     style="width:40px; height:40px; border-radius:50%; object-fit:cover;" 
                     onerror="this.onerror=null;this.src='images/default-user.png';" />
              </td>
              <td><%= user.getName() %></td>
              <td><%= user.getEmail() %></td>
              <td><%= user.getRole() %></td>
              <td>
                <span class="status active">
                  <i class="fas fa-check-circle"></i> Active
                </span>
              </td>
              <% if (isAdmin) { %>
              <td>
                <!-- Edit button -->
                <form action="edituser.jsp" method="get" style="display:inline;">
                  <input type="hidden" name="email" value="<%= user.getEmail() %>" />
                  <button type="submit" class="btn btn-outline btn-sm">
                    <i class="fas fa-edit"></i> Edit
                  </button>
                </form>

                <!-- Delete button -->
                <form action="deleteUser" method="post" class="delete-form" style="display:inline;">
                  <input type="hidden" name="email" value="<%= user.getEmail() %>" />
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

<!-- JQuery -->
<script src="vendor/jquery/jquery.min.js"></script>

<!-- SweetAlert2 CDN -->
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<script>
  // Show status alerts based on query param
  const urlParams = new URLSearchParams(window.location.search);
  const status = urlParams.get("status");

  if (status === "success") {
    Swal.fire("Success!", "User updated successfully!", "success");
  } else if (status === "delete_success") {
    Swal.fire("Deleted!", "User deleted successfully!", "success");
  } else if (status === "failed") {
    Swal.fire("Error", "Operation failed.", "error");
  } else if (status === "invalid") {
    Swal.fire("Warning", "Invalid user email.", "warning");
  }

  // Confirm deletion using SweetAlert2
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

