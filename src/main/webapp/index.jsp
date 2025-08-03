<%@ page import="com.pahanaedu.dao.UserDao" %>
<%
  if (session.getAttribute("user") == null) {
      response.sendRedirect("login.jsp");
      return; // Prevent further processing if not logged in
  }

  // Get total user count dynamically
  int totalUsers = UserDao.getUserCount();
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>Admin Dashboard</title>

  <!-- Font Awesome -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" />
  <link rel="icon" type="image/x-icon" href="images/favicon.png"> 
  <!-- Your custom CSS -->
  <link rel="stylesheet" href="css/dashboard.css" />
</head>
<body>
  <div class="container">

    <%@ include file="sidebar.jsp" %>
    <%@ include file="header.jsp" %>


      <!-- Stats Cards -->
<div class="stats-cards">
  <!-- Total Users (Dynamic) -->
  <div class="stat-card">
    <div class="card-header">
      <div>
        <div class="card-value"><%= totalUsers %></div>
        <div class="card-label">Total Users</div>
      </div>
      <div class="card-icon purple">
        <i class="fas fa-users"></i>
      </div>
    </div>
    <div class="card-change positive">
      <i class="fas fa-arrow-up"></i>
      <span>12.5% from last month</span>
    </div>
  </div>

  <!-- Static Card 1: Active Sessions -->
  <div class="stat-card">
    <div class="card-header">
      <div>
        <div class="card-value">23</div>
        <div class="card-label">Active Sessions</div>
      </div>
      <div class="card-icon green">
        <i class="fas fa-chart-line"></i>
      </div>
    </div>
    <div class="card-change negative">
      <i class="fas fa-arrow-down"></i>
      <span>4.1% from last week</span>
    </div>
  </div>

  <!-- Static Card 2: New Signups -->
  <div class="stat-card">
    <div class="card-header">
      <div>
        <div class="card-value">8</div>
        <div class="card-label">New Signups</div>
      </div>
      <div class="card-icon blue">
        <i class="fas fa-user-plus"></i>
      </div>
    </div>
    <div class="card-change positive">
      <i class="fas fa-arrow-up"></i>
      <span>22.8% increase</span>
    </div>
  </div>

  <!-- Static Card 3: Monthly Visits -->
  <div class="stat-card">
    <div class="card-header">
      <div>
        <div class="card-value">1,024</div>
        <div class="card-label">Monthly Visits</div>
      </div>
      <div class="card-icon orange">
        <i class="fas fa-eye"></i>
      </div>
    </div>
    <div class="card-change positive">
      <i class="fas fa-arrow-up"></i>
      <span>5.6% growth</span>
    </div>
  </div>
</div>

      <!-- Recent Orders Table (unchanged) -->
      <div class="table-card">
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
            <tr>
              <td>#ORD-004</td>
              <td>Sarah Davis</td>
              <td>12 Mar 2025</td>
              <td>$350.00</td>
              <td><span class="status cancelled"><i class="fas fa-times-circle"></i> Cancelled</span></td>
              <td><button class="btn btn-outline btn-sm"><i class="fas fa-eye"></i> View</button></td>
            </tr>
            <tr>
              <td>#ORD-005</td>
              <td>David Wilson</td>
              <td>11 Mar 2025</td>
              <td>$185.25</td>
              <td><span class="status active"><i class="fas fa-check-circle"></i> Completed</span></td>
              <td><button class="btn btn-outline btn-sm"><i class="fas fa-eye"></i> View</button></td>
            </tr>
          </tbody>
        </table>
      </div>

    </div>
  </div>
</body>
</html>

