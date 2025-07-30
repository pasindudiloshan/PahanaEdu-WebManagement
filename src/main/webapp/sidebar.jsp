<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String currentPage = request.getRequestURI();
%>
<div class="sidebar">
  <div class="logo">
    <a href="index.jsp">
      <img src="images/pahanaedu_logo.png" alt="PahanaEdu Logo" style="width: 100%; max-width: 180px;" />
    </a>
  </div>
  <div class="nav-menu">
    <div class="menu-heading">Main</div>
    <a href="index.jsp" class="nav-item <%= currentPage.endsWith("index.jsp") ? "active" : "" %>">
      <i class="fas fa-chart-pie"></i>
      <span>Dashboard</span>
    </a>
    <a href="customermanagement.jsp" class="nav-item <%= currentPage.endsWith("customermanagement.jsp") ? "active" : "" %>">
      <i class="fas fa-user-friends"></i>
      <span>Customers</span>
    </a>
    <a href="productmanagement.jsp" class="nav-item <%= currentPage.endsWith("productmanagement.jsp") ? "active" : "" %>">
      <i class="fas fa-box"></i>
      <span>Products</span>
    </a>

    <div class="menu-heading">Reports</div>
    <div class="nav-item <%= currentPage.endsWith("sales.jsp") ? "active" : "" %>">
      <i class="fas fa-coins"></i>
      <span>Sales</span>
    </div>
    <div class="nav-item <%= currentPage.endsWith("bills.jsp") ? "active" : "" %>">
      <i class="fas fa-file-invoice-dollar"></i>
      <span>Bills</span>
    </div>

    <div class="menu-heading">Admin</div>
    <a href="usermanagement.jsp" class="nav-item <%= currentPage.endsWith("usermanagement.jsp") ? "active" : "" %>">
      <i class="fas fa-users-cog"></i>
      <span>Users</span>
    </a>
    <div class="nav-item <%= currentPage.endsWith("faq.jsp") ? "active" : "" %>">
      <i class="fas fa-question-circle"></i>
      <span>FAQ</span>
    </div>
    <div class="nav-item <%= currentPage.endsWith("settings.jsp") ? "active" : "" %>">
      <i class="fas fa-cogs"></i>
      <span>Settings</span>
    </div>
  </div>
</div>
