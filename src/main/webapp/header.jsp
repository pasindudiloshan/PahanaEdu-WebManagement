<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%!
// Declare user as a page-level variable just once
com.pahanaedu.model.User user = null;
%>

<%
  // Initialize user from session (if exists)
  if (session.getAttribute("user") != null) {
    user = (com.pahanaedu.model.User) session.getAttribute("user");
  }
%>

<div class="header">
  <div class="search-bar">
    <i class="fas fa-search"></i>
    <input type="text" placeholder="Search..." />
  </div>
  <div class="header-actions">
    <div class="notification">
      <i class="fas fa-bell"></i>
      <div class="badge">3</div>
    </div>
    <div class="notification">
      <i class="fas fa-envelope"></i>
      <div class="badge">5</div>
    </div>
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

    <!-- Logout button -->
    <div class="logout-wrapper">
      <form action="Logout" method="get" class="logout-form">
        <button type="submit" class="logout-button">
          <i class="fas fa-sign-out-alt"></i> Logout
        </button>
      </form>
    </div>
  </div>
</div>
