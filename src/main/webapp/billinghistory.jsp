<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.ArrayList, java.text.SimpleDateFormat" %>
<%@ page import="java.sql.Connection, java.sql.SQLException" %>
<%@ page import="com.pahanaedu.model.Bill, com.pahanaedu.model.User" %>
<%@ page import="com.pahanaedu.dao.BillDao" %>
<%@ page import="com.pahanaedu.util.DBUtil" %>

<%
    User user = (User) session.getAttribute("user");
    if (user == null || !"Admin".equalsIgnoreCase(user.getRole())) {
        response.sendRedirect("login.jsp");
        return;
    }

    Connection conn = null;
    BillDao billDao = null;
    List<Bill> bills = new ArrayList<>();
    String filterAccNo = request.getParameter("accountNumber");
    String filterDateStr = request.getParameter("billDate");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    java.util.Date filterDate = null;

    try {
        if (filterDateStr != null && !filterDateStr.isEmpty()) {
            filterDate = sdf.parse(filterDateStr);
        }

        conn = DBUtil.getConnection();
        billDao = new BillDao(conn);

        if ((filterAccNo != null && !filterAccNo.trim().isEmpty()) || filterDate != null) {
            bills = billDao.getFilteredBills(filterAccNo, filterDate);  
        } else {
            bills = billDao.getAllBills();
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Billing History</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"/>
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
            <div class="title"><i class="fas fa-file-invoice-dollar"></i> Billing History</div>
        </div>

        <%-- Show Final Amount Message --%>
        <%
            Object finalAmount = request.getAttribute("finalAmount");
            if (finalAmount != null) {
        %>
            <div class="alert-success" style="margin-bottom: 20px; background: #e9fbe9; padding: 12px; border-left: 5px solid green;">
                ✅ Bill Created Successfully. Final Amount: <strong>Rs. <%= String.format("%.2f", finalAmount) %></strong>
            </div>
        <% } %>

        <!-- Filter Form -->
        <form method="get" class="form-row-two" style="margin-bottom: 20px;">
            <div class="form-group">
                <label for="accountNumber">Filter by Account Number:</label>
                <input type="text" name="accountNumber" id="uempid" class="form-input"
                       value="<%= (filterAccNo != null) ? filterAccNo : "PEB-ACC-" %>"
                       placeholder="PEB-ACC-XXX">
            </div>
            <div class="form-group">
                <label for="billDate">Filter by Date:</label>
                <input type="date" name="billDate" id="billDate" class="form-input"
                       value="<%= (filterDateStr != null) ? filterDateStr : "" %>">
            </div>
            <div style="display: flex; align-items: flex-end; gap: 10px;">
                <button type="submit" class="btn btn-primary"><i class="fas fa-filter"></i> Filter</button>
                <a href="billinghistory.jsp" class="btn btn-outline"><i class="fas fa-times"></i> Clear</a>
            </div>
        </form>

        <!-- Table Card -->
        <div class="table-card">
            <div class="card-title">
                <i class="fas fa-list-alt"></i> All Bills
            </div>
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Bill ID</th>
                        <th>Account Number</th>
                        <th>Date</th>
                        <th>Payment Method</th>
                        <th>Discount (%)</th>
                        <th>Total Amount</th>
                        <th>Final Amount</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (bills != null && !bills.isEmpty()) {
                            for (Bill b : bills) {
                    %>
                        <tr>
                            <td><%= b.getId() %></td>
                            <td><%= b.getAccountNumber() %></td>
                            <td><%= (b.getBillingDate() != null) ? sdf.format(b.getBillingDate()) : "" %></td>
                            <td><%= b.getPaymentMethod() %></td>
                            <td><%= String.format("%.2f", b.getDiscountPercent()) %></td>
                            <td>Rs. <%= String.format("%.2f", b.getTotalAmount()) %></td>
                            <td>Rs. <%= String.format("%.2f", b.getFinalAmount()) %></td>
                            <td>
                                <a href="viewbill.jsp?id=<%= b.getId() %>" class="btn btn-sm btn-outline">
                                    <i class="fas fa-eye"></i> View
                                </a>
                            </td>
                        </tr>
                    <%
                            }
                        } else {
                    %>
                        <tr>
                            <td colspan="8" class="text-center text-muted">No bills found.</td>
                        </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- JS -->
<script src="vendor/jquery/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script src="js/main.js"></script>

<script>
  const prefix = "PEB-ACC-";
  const empInput = document.getElementById("uempid");

  window.addEventListener("DOMContentLoaded", () => {
    if (!empInput.value.startsWith(prefix)) {
      empInput.value = prefix;
      empInput.setSelectionRange(prefix.length, prefix.length);
    }
  });

  empInput.addEventListener("keydown", (e) => {
    const cursor = empInput.selectionStart;
    if ((e.key === "Backspace" || e.key === "ArrowLeft") && cursor <= prefix.length) {
      e.preventDefault();
    }
    if (cursor < prefix.length && e.key.length === 1) {
      e.preventDefault();
    }
  });

  empInput.addEventListener("input", () => {
    if (!empInput.value.startsWith(prefix)) {
      const digits = empInput.value.replace(/[^0-9]/g, "").slice(0, 3);
      empInput.value = prefix + digits;
    }
  });
</script>

</body>
</html>

