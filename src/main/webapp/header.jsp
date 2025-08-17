<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%!
// Declare user as a page-level variable
com.pahanaedu.model.User user = null;
%>

<%
  // Initialize user from session (if exists)
  if (session.getAttribute("user") != null) {
    user = (com.pahanaedu.model.User) session.getAttribute("user");
  }
%>

<!-- SweetAlert2 CSS & JS -->
<link href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<div class="header">
  <div class="search-bar">
    <i class="fas fa-search"></i>
    <input type="text" placeholder="Search..." />
  </div>

  <div class="header-actions">
    <!-- Notifications -->
    <div class="notification">
      <i class="fas fa-bell"></i>
      <div class="badge">3</div>
    </div>
    <div class="notification">
      <i class="fas fa-envelope"></i>
      <div class="badge">5</div>
    </div>

    <!-- User Profile -->
    <div class="user-profile">
      <div class="profile-img">
        <img 
          src="userImage?email=<%= (user != null) ? user.getEmail() : "" %>" 
          alt="User Image" 
          style="width:40px; height:40px; border-radius:50%; object-fit:cover;" 
          onerror="this.onerror=null;this.src='images/default-user.png';" 
        />
      </div>
      <div class="user-info">
        <div class="user-name">
          <%= (user != null) ? user.getName() : "Guest" %>
        </div>
        <div class="user-role">
          <%= (user != null) ? user.getRole() : "Guest" %>
        </div>
      </div>
    </div>

    <!-- Logout Button -->
    <div class="logout-wrapper">
      <form id="logoutForm" action="Logout" method="get" class="logout-form">
        <button type="button" id="logoutButton" class="logout-button">
          <i class="fas fa-sign-out-alt"></i> Logout
        </button>
      </form>
    </div>
  </div>
</div>

<!-- SweetAlert2 Logout Script -->
<script>
document.getElementById("logoutButton").addEventListener("click", function() {
    Swal.fire({
        title: 'Are you sure?',
        text: "Do you really want to logout?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, logout',
        cancelButtonText: 'No, stay logged in'
    }).then((result) => {
        if (result.isConfirmed) {
            document.getElementById("logoutForm").submit();
        }
    });
});
</script>

