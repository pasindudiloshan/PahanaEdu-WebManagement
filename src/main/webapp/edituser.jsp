<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, com.pahanaedu.util.DBUtil, com.pahanaedu.model.User" %>

<%
    User currentUser = (User) session.getAttribute("user");
    if (currentUser == null) {
        response.sendRedirect("login.jsp");
        return;
    } else if (!"Admin".equalsIgnoreCase(currentUser.getRole())) {
        response.sendRedirect("index.jsp");
        return;
    }

    String email = request.getParameter("email");
    String name = "", contact = "", role = "", uempid = "";

    if (email != null && !email.isEmpty()) {
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement("SELECT * FROM users WHERE uemail = ?")) {

            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                name = rs.getString("uname");
                contact = rs.getString("umobile");
                role = rs.getString("urole");
                uempid = rs.getString("uempid");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <title>Edit User</title>
  <link rel="stylesheet" href="css/user-management.css" />
  <link rel="stylesheet" href="css/sidebar-header.css" />
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" />
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css" />
</head>
<body>
<div class="container">
  <%@ include file="sidebar.jsp" %>
  <%@ include file="header.jsp" %>

  <div class="main-content">
    <div class="page-title">
      <div class="title">Edit User</div>
      <div class="action-buttons">
        <a href="usermanagement.jsp" class="btn btn-outline">
          <i class="fas fa-arrow-left"></i> Back
        </a>
      </div>
    </div>

    <div class="table-card">
      <div class="card-title"><h3><i class="fas fa-user-edit"></i> Update User Details</h3></div>

      <div style="padding: 24px;">
        <form action="UpdateUserServlet" method="post" enctype="multipart/form-data">
          <input type="hidden" name="originalEmail" value="<%= email %>" />

          <div class="form-row-two">
            <div class="form-group">
              <label for="uempid">Employee ID <span style="color:red">*</span></label>
              <input type="text" name="uempid" id="uempid" value="<%= uempid %>" class="form-input"  required />
            </div>
            <div class="form-group">
              <label for="name">Full Name <span style="color:red">*</span></label>
              <input type="text" name="name" id="name" value="<%= name %>" class="form-input" required />
            </div>
          </div>

          <div class="form-row-two">
            <div class="form-group">
              <label for="email">Email Address <span style="color:red">*</span></label>
              <input type="email" name="email" id="email" value="<%= email %>" class="form-input" required />
            </div>
            <div class="form-group">
              <label for="contact">Contact Number <span style="color:red">*</span></label>
              <input type="text" name="contact" id="contact" value="<%= contact %>" class="form-input" pattern="^\d{10,15}$" required />
            </div>
          </div>

          <div class="form-row-two">
            <div class="form-group">
              <label for="role">Role <span style="color:red">*</span></label>
              <select name="role" id="role" class="form-input" required>
                <option value="Admin" <%= "Admin".equalsIgnoreCase(role) ? "selected" : "" %>>Admin</option>
                <option value="Employee" <%= "Employee".equalsIgnoreCase(role) ? "selected" : "" %>>Employee</option>
              </select>
            </div>
            <div class="form-group">
              <label for="photo">Profile Picture</label>
              <input type="file" name="photo" id="photo" class="form-input" accept="image/*" />
            </div>
          </div>

          <div class="form-row" style="margin-top: 30px;">
            <button type="submit" class="btn btn-primary">
              <i class="fas fa-save"></i> Update User
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="js/main.js"></script>
</body>
</html>


