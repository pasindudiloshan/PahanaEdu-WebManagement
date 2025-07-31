<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, java.sql.*, java.text.*" %>
<%@ page import="com.pahanaedu.model.*" %>
<%@ page import="com.pahanaedu.dao.*" %>
<%@ page import="com.pahanaedu.util.DBUtil" %>

<%
    String billIdParam = request.getParameter("id");
    if (billIdParam == null) {
        response.sendRedirect("billinghistory.jsp");
        return;
    }

    int billId = -1;
    try {
        billId = Integer.parseInt(billIdParam);
    } catch (NumberFormatException e) {
        response.sendRedirect("billinghistory.jsp");
        return;
    }

    Bill bill = null;
    List<BillItem> items = new ArrayList<>();
    Customer customer = null;
    Map<String, Product> productMap = new HashMap<>();

    DecimalFormat df = new DecimalFormat("0.00");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    try (Connection conn = DBUtil.getConnection()) {
        BillDao billDao = new BillDao(conn);
        bill = billDao.getBillById(billId);

        if (bill == null) {
            response.sendRedirect("billinghistory.jsp");
            return;
        }

        items = billDao.getBillItems(billId);
        customer = CustomerDao.getCustomerByAccount(bill.getAccountNumber()); // ✅ static call

        List<Product> allProducts = ProductDao.getAllProducts(); // ✅ static call
        for (Product p : allProducts) {
            productMap.put(p.getItemId(), p); // ✅ assuming getItemId() returns String
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Bill #<%= billId %> - Invoice</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
  <link rel="stylesheet" href="css/sidebar-header.css">
  <link rel="stylesheet" href="css/view-bill.css">
  <link rel="icon" type="image/x-icon" href="images/favicon.png">
</head>
<body>

<div class="container">
  <%@ include file="sidebar.jsp" %>
  <%@ include file="header.jsp" %>

  <div class="main-content">
    <div class="page-title">
      <div class="title"><i class="fas fa-file-invoice"></i> Bill Details - #<%= billId %></div>
      <div class="action-buttons">
        <a href="billinghistory.jsp" class="btn btn-outline">
          <i class="fas fa-arrow-left"></i> Back to History
        </a>
      </div>
    </div>

    <div class="invoice-card">
      <div class="section-header">Customer Information</div>
      <div class="info-grid">
        <p><strong>Account No:</strong> <%= bill.getAccountNumber() %></p>
        <p><strong>Customer Name:</strong> <%= (customer != null) ? customer.getFullName() : "Unknown Customer" %></p>
        <p><strong>Billing Date:</strong> <%= (bill.getBillingDate() != null) ? sdf.format(bill.getBillingDate()) : "" %></p>
        <p><strong>Payment Method:</strong> <%= bill.getPaymentMethod() %></p>
        <p><strong>Discount:</strong> <%= df.format(bill.getDiscountPercent()) %> %</p>
      </div>

      <div class="section-header" style="margin-top:30px;">Billed Items</div>
      <table class="invoice-table">
        <thead>
          <tr>
            <th>Product</th>
            <th>Quantity</th>
            <th>Unit Price (Rs.)</th>
            <th>Total (Rs.)</th>
          </tr>
        </thead>
        <tbody>
          <% for (BillItem item : items) {
               Product product = productMap.get(item.getProductId());
               String name = (product != null) ? product.getName() : "Product #" + item.getProductId();
          %>
            <tr>
              <td><%= name %></td>
              <td><%= item.getQuantity() %></td>
              <td><%= df.format(item.getUnitPrice()) %></td>
              <td><%= df.format(item.getUnitPrice() * item.getQuantity()) %></td>
            </tr>
          <% } %>
        </tbody>
      </table>

      <div class="totals">
        <p><strong>Total (Before Discount):</strong> Rs. <%= df.format(bill.getTotalAmount()) %></p>
        <p><strong>Final Amount (After Discount):</strong> Rs. <%= df.format(bill.getFinalAmount()) %></p>
      </div>
    </div>
  </div>
</div>

</body>
</html>

