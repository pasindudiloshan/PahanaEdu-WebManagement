<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.pahanaedu.model.User" %>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Choose Your Role</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <link rel="icon" href="images/favicon.png" />
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" />
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" />
  <link rel="stylesheet" href="css/sidebar-header.css">
  <link rel="stylesheet" href="css/bill.css">
  <link rel="stylesheet" href="css/help.css" />

  <script src="https://code.jquery.com/jquery-3.6.0.min.js" defer></script>
  <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js" defer></script>
  <script src="js/bill.js" defer></script>
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>

<body>

<div class="container">
  <%@ include file="sidebar.jsp" %>
  <%@ include file="header.jsp" %>

  <%
      User loggedUser = (User) session.getAttribute("user");
      if (loggedUser == null) {
          response.sendRedirect("login.jsp");
          return;
      }
      boolean isAdmin = "Admin".equalsIgnoreCase(loggedUser.getRole());
  %>

  <div class="main-content">
    <div class="page-title">
      <div class="title">Help Section</div>   
    </div>

    <!-- Tutorial Selection Section -->
    <div class="tutorial-section">
        <h2 class="section-title">Choose Your Tutorial</h2>

        <div class="tutorial-buttons">
            
            <!-- Codex (Visible to all employees) -->
<div class="tutorial-container">
    <a href="helpemp.jsp" class="tutorial-button">
        <img src="images/codex.png" alt="PahanaEdu Codex Tutorial" />
        <strong>PahanaEdu Billing System Tutorial with "Codex"</strong>
    </a>
    <p class="role-label">For Employees</p>
</div>

            <!-- Codex Pro (Visible only to Admin) -->
            <% if (isAdmin) { %>
            <div class="tutorial-container">
                     <a href="helpadmin.jsp" class="tutorial-button">
                    <img src="images/codexpro.png" alt="PahanaEdu Codex Pro Tutorial" />
                    <strong>PahanaEdu System Tutorial with "Codex-Pro"</strong>
                </a>
                <p class="role-label">For Admins</p>
            </div>
            <% } %>

        </div>
    </div>
    <!-- End Tutorial Section -->

  </div>
</div>

</body>
</html>


