<%@ page import="com.pahanaedu.dao.CustomerDao" %> 
<%@ page import="com.pahanaedu.dao.UserDao" %>
<%@ page import="com.pahanaedu.dao.ProductDao" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    int totalCustomers = CustomerDao.getCustomerCount(); 
    int totalUsers = UserDao.getUserCount();
    int totalProducts = ProductDao.getProductCount();
    
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Admin Dashboard</title>

  <link rel="icon" href="<%= request.getContextPath() %>/images/favicon.png" type="image/x-icon" />

  <!-- Font Awesome -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" />

  <!-- Styles -->
  <link rel="stylesheet" href="css/sidebar-header.css" />
  <link rel="stylesheet" href="css/index.css" />
</head>
<body>

<div class="container">
  <%@ include file="sidebar.jsp" %>
  <%@ include file="header.jsp" %>

  <div class="main-content">
    
    <div class="page-title">
      <div class="title">Dashboard</div>
    </div>

   <section class="stats-cards">
   
     <div class="stat-card">
    <div class="card-header">
      <div>
        <div class="card-value"><%= totalCustomers %></div>
        <div class="card-label">Total Customers</div>
      </div>
      <div class="card-icon blue">
        <i class="fas fa-users"></i>
      </div>
    </div>
    <div class="card-change positive">
      <i class="fas fa-arrow-up"></i>
      <span>9.8% from last month</span>
    </div>
  </div>
  
    <div class="stat-card">
    <div class="card-header">
      <div>
        <div class="card-value"><%= totalProducts %></div>
        <div class="card-label">Total Products</div>
      </div>
      <div class="card-icon green">
        <i class="fas fa-book"></i>
      </div>
    </div>
    <div class="card-change positive">
      <i class="fas fa-arrow-up"></i>
      <span>8.9% from last month</span>
    </div>
  </div>
  
  <div class="stat-card">
    <div class="card-header">
      <div>
        <div class="card-value"><%= totalUsers %></div>
        <div class="card-label">Total Users</div>
      </div>
      <div class="card-icon purple">
        <i class="fas fa-users-cog"></i> 
      </div>
    </div>
    <div class="card-change positive">
      <i class="fas fa-arrow-up"></i>
      <span>12.5% from last month</span>
    </div>
  </div>

</section>


    <!-- Recent Orders -->
    <section class="table-card">
      <div class="card-title">
        <h3><i class="fas fa-shopping-bag"></i> Recent Orders</h3>
        <button class="btn btn-outline btn-sm">
          <i class="fas fa-eye"></i> View All
        </button>
      </div>
      <table class="data-table">
        <thead>
          <tr>
            <th>Order ID</th>
            <th>Customer</th>
            <th>Date</th>
            <th>Amount</th>
            <th>Status</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <td>#ORD-001</td>
            <td>John Smith</td>
            <td>15 Mar 2025</td>
            <td>$125.00</td>
            <td><span class="status active"><i class="fas fa-check-circle"></i> Completed</span></td>
            <td><button class="btn btn-outline btn-sm"><i class="fas fa-eye"></i> View</button></td>
          </tr>
          <tr>
            <td>#ORD-002</td>
            <td>Emma Johnson</td>
            <td>14 Mar 2025</td>
            <td>$245.99</td>
            <td><span class="status pending"><i class="fas fa-clock"></i> Pending</span></td>
            <td><button class="btn btn-outline btn-sm"><i class="fas fa-eye"></i> View</button></td>
          </tr>
          <tr>
            <td>#ORD-003</td>
            <td>Michael Brown</td>
            <td>13 Mar 2025</td>
            <td>$79.50</td>
            <td><span class="status active"><i class="fas fa-check-circle"></i> Completed</span></td>
            <td><button class="btn btn-outline btn-sm"><i class="fas fa-eye"></i> View</button></td>
          </tr>
        </tbody>
      </table>
    </section>

  </div>
</div>

</body>
</html>
