<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="
    java.util.List,
    com.pahanaedu.model.User,
    com.pahanaedu.model.Customer,
    com.pahanaedu.model.Product,
    com.pahanaedu.dao.CustomerDao,
    com.pahanaedu.dao.ProductDao
" %>

<%
    User loggedUser = (User) session.getAttribute("user");
    if (loggedUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    List<Customer> customers = CustomerDao.getAllCustomers();
    List<Product> products = ProductDao.getAllProducts();
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Create New Bill</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <link rel="icon" href="images/favicon.png" />
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" />
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" />
  <link rel="stylesheet" href="css/sidebar-header.css">
  <link rel="stylesheet" href="css/bill.css">
  

  <script src="https://code.jquery.com/jquery-3.6.0.min.js" defer></script>
  <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js" defer></script>
  <script src="js/bill.js" defer></script>
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>

<body>

<div class="container">
  <%@ include file="sidebar.jsp" %>
  <%@ include file="header.jsp" %>

  <div class="main-content">
    <div class="page-title">
      <div class="title">Create New Bill</div>
      <div class="action-buttons">
        <a href="billinghistory.jsp" class="btn btn-outline"><i class="fas fa-arrow-left"></i> Back</a>
      </div>
    </div>

    <div class="table-card">
      <div class="card-title">
        <h3><i class="fas fa-file-invoice-dollar"></i> Bill Details</h3>
      </div>

      <form id="billForm" action="addBill" method="post" onsubmit="return validateBeforeSubmit()">
        <!-- Customer & Product -->
        <div class="form-row form-row-wrap mb-3" style="gap: 20px;">
          <div class="form-group">
            <label for="accountNumber">Customer <span class="text-danger">*</span></label>
            <select name="accountNumber" id="accountNumber" class="form-input select2 bold-select" required>
              <option value="">-- Select Customer --</option>
              <% for (Customer c : customers) { %>
                <option value="<%= c.getAccountNumber() %>">
                  <%= c.getFullName() %> (<%= c.getAccountNumber() %>)
                </option>
              <% } %>
            </select>
          </div>

          <div class="form-group">
            <label for="productSelect">Product <span class="text-danger">*</span></label>
            <select id="productSelect" class="form-input select2 bold-select">
              <option value="">-- Select Product --</option>
              <% for (Product p : products) { %>
                <option value="<%= p.getItemId() %>" data-price="<%= p.getPrice() %>" data-stock="<%= p.getQuantity() %>">
                  <%= p.getName() %> - Rs. <%= String.format("%.2f", p.getPrice()) %> (Stock: <%= p.getQuantity() %>)
                </option>
              <% } %>
            </select>
          </div>
        </div>

        <!-- Quantity, Payment & Discount -->
        <div class="form-row form-row-wrap mb-3" style="gap: 20px;">
          <div class="form-group">
            <label>Qty</label>
            <input type="number" id="quantityInput" class="form-input" value="1" min="1" />
          </div>

          <div class="form-group">
            <label>Unit Price</label>
            <input type="number" id="unitPriceInput" class="form-input" readonly />
          </div>

          <div class="form-group">
            <label>Payment Method</label>
            <select name="paymentMethod" id="paymentMethod" class="form-input" onchange="setDiscount()" required>
              <option value="Cash">Cash</option>
              <option value="Credit Card">Credit Card</option>
              <option value="Debit Card">Debit Card</option>
              <option value="Bank Transfer">Bank Transfer</option>
            </select>
          </div>

          <div class="form-group">
            <label>Discount %</label>
            <input type="number" name="discountPercent" id="discountPercent" class="form-input" value="0.00" readonly />
          </div>

          <div class="form-group button-group">
            <label>&nbsp;</label>
            <button type="button" class="btn btn-outline btn-sm" onclick="calculateSubtotal()">Apply</button>
          </div>
        </div>

        <!-- Subtotals -->
        <div class="form-row form-row-wrap mb-4" style="gap: 20px;">
          <div class="form-group">
            <label>Subtotal</label>
            <input type="text" id="subtotalOutput" class="form-input" readonly />
          </div>

          <div class="form-group">
            <label>Final Price</label>
            <input type="text" id="finalOutput" class="form-input" readonly />
            <input type="hidden" name="finalAmount" id="finalAmountInput" />
          </div>

          <div class="form-group button-group">
            <label>&nbsp;</label>
            <button type="button" class="btn btn-success btn-sm" onclick="addProduct()">Add To Table</button>
          </div>
        </div>

        <!-- Product Table -->
        <div class="mb-3">
          <h5>Product List</h5>
          <table class="data-table" id="productTable">
            <thead>
              <tr>
                <th>Product</th>
                <th>Qty</th>
                <th>Unit Price</th>
                <th>Subtotal</th>
                <th>Discount</th>
                <th>Final Price</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody id="productTableBody"></tbody>
          </table>
        </div>

        <!-- Grand Total -->
        <div class="text-end mb-4">
          <strong>Grand Total: <span id="grandTotal">Rs. 0.00</span></strong>
        </div>

        <!-- Submit -->
        <div class="form-group button-group" style="text-align: right;">
          <button type="submit" class="btn btn-primary">
            <i class="fas fa-check-circle"></i> Create Bill
          </button>
        </div>
      </form>
    </div>
  </div>
</div>

</body>
</html>

