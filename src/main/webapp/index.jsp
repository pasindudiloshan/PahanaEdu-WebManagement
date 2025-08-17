<%@ page import="com.pahanaedu.dao.CustomerDao" %> 
<%@ page import="com.pahanaedu.dao.UserDao" %>
<%@ page import="com.pahanaedu.dao.ProductDao" %>
<%@ page import="com.pahanaedu.dao.BillDao" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.util.List" %>
<%@ page import="com.pahanaedu.model.Bill, com.pahanaedu.model.Customer" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.pahanaedu.util.DBUtil" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // ✅ Handle login success status
    String status = (String) session.getAttribute("status");
    if (status != null) {
        session.removeAttribute("status"); // clear after using
    }

    int totalCustomers = CustomerDao.getCustomerCount(); 
    int totalUsers = UserDao.getUserCount();
    int totalProducts = ProductDao.getProductCount();

    Connection conn = null;
    BillDao billDao = null;
    double totalSales = 0.0;
    double totalRevenue = 0.0;
    int totalOrders = 0;
    List<Bill> recentBills = null;
    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

    try {
        conn = DBUtil.getConnection();
        billDao = new BillDao(conn);
        totalSales = billDao.getTotalSales();
        totalRevenue = totalSales * 0.15;
        totalOrders = billDao.getTotalOrderCount();
        recentBills = billDao.getRecentBills(5);
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        if (conn != null) try { conn.close(); } catch(Exception e) { e.printStackTrace(); }
    }
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

  <!-- SweetAlert -->
  <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
</head>
<body>

<!-- Hidden status for JS -->
<input type="hidden" id="status" value="<%= status != null ? status : "" %>" />

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
      <div class="card-value">Rs. <%= String.format("%.2f", totalRevenue) %></div>
      <div class="card-label">Total Revenue</div>
    </div>
    <div class="card-icon green">
      <i class="fas fa-coins"></i>
    </div>
  </div>
  <div class="card-change positive">
    <i class="fas fa-arrow-up"></i>
    <span>10.0% from last month</span>
  </div>
</div>

<div class="stat-card">
  <div class="card-header">
    <div>
      <div class="card-value"><%= totalOrders %></div>
      <div class="card-label">Total Orders</div>
    </div>
    <div class="card-icon yellow">
      <i class="fas fa-shopping-cart"></i>
    </div>
  </div>
  <div class="card-change positive">
    <i class="fas fa-arrow-up"></i>
    <span>7.3% from last month</span>
  </div>
</div>

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
    <button class="btn btn-outline btn-sm" onclick="location.href='billinghistory.jsp'">
      <i class="fas fa-eye"></i> View All
    </button>
  </div>
  
<table class="data-table">
  <thead>
    <tr>
      <th>Order ID</th>
      <th>Profile Picture</th>   
      <th>Account Number</th>   
      <th>Customer</th>
      <th>Date</th>
      <th>Purchase Amount</th>
      <th>Actions</th>
    </tr>
  </thead>
  <tbody>
  <%
    if (recentBills != null && !recentBills.isEmpty()) {
        for (Bill b : recentBills) {
            Customer c = CustomerDao.getCustomerByAccount(b.getAccountNumber());
            String custName = (c != null) ? c.getFullName() : "Unknown";
  %>
  <tr>
    <td>PEB-ORD-<%= String.format("%03d", b.getId()) %></td>
    <td>
      <img src="customerImage?account=<%= b.getAccountNumber() %>" 
           alt="Customer Image" 
           style="width:35px; height:35px; border-radius:50%; object-fit:cover;">
    </td>
    <td><%= b.getAccountNumber() %></td>
    <td><%= custName %></td>
    <td><%= (b.getBillingDate() != null) ? sdf.format(b.getBillingDate()) : "" %></td>
    <td>Rs. <%= String.format("%.2f", b.getFinalAmount()) %></td>
    <td>
      <a href="viewbill.jsp?id=<%= b.getId() %>" class="btn btn-outline btn-sm">
        <i class="fas fa-eye"></i> View
      </a>
    </td>
  </tr>
  <%
        }
    } else {
  %>
  <tr>
    <td colspan="7" style="text-align:center;">No recent orders found.</td>
  </tr>
  <%
    }
  %>
  </tbody>
</table>

</section>

  </div>
</div>


<script>
const status = document.getElementById("status").value;
if (status === "success") {
  swal("Welcome!", "Login Successful", "success");
}
</script>

</body>
</html>
