<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.pahanaedu.model.User, com.pahanaedu.model.Customer, com.pahanaedu.model.Product" %>
<%@ page import="com.pahanaedu.dao.CustomerDao, com.pahanaedu.dao.ProductDao" %>

<%
    User loggedUser = (User) session.getAttribute("user");
    if (loggedUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    if (!"Admin".equalsIgnoreCase(loggedUser.getRole())) {
        response.sendRedirect("index.jsp");
        return;
    }

    List<Customer> customers = CustomerDao.getAllCustomers();
    List<Product> products = ProductDao.getAllProducts();
%>

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Create New Bill</title>
  <link rel="stylesheet" href="css/product-management.css" />
  <link rel="stylesheet" href="css/sidebar-header.css" />
  <link rel="icon" type="image/x-icon" href="images/favicon.png">
  <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
</head>
<body>

<div class="container">
  <%@ include file="sidebar.jsp" %>
  <%@ include file="header.jsp" %>

  <div class="main-content">
    <div class="page-title">
      <div class="title">Create New Bill</div>
      <div class="action-buttons">
        <a href="billinghistory.jsp" class="btn btn-outline">
          <i class="fas fa-arrow-left"></i> Back
        </a>
      </div>
    </div>

    <div class="table-card">
      <div class="card-title">
        <h3><i class="fas fa-file-invoice-dollar"></i> Bill Details</h3>
      </div>

      <div style="padding: 24px;">
        <form action="addBill" method="post" onsubmit="return validateBeforeSubmit()">
          <div class="form-row-two">
            <div class="form-group">
              <label for="accountNumber">Customer <span style="color:red">*</span></label>
              <select name="accountNumber" id="accountNumber" class="form-input select2" required>
                <option value="">-- Select Customer --</option>
                <% for (Customer c : customers) { %>
                  <option value="<%= c.getAccountNumber() %>">
                    <%= c.getFullName() %> (<%= c.getAccountNumber() %>)
                  </option>
                <% } %>
              </select>
            </div>
            <div class="form-group">
              <label for="paymentMethod">Payment Method <span style="color:red">*</span></label>
              <select name="paymentMethod" id="paymentMethod" class="form-input" required onchange="setDiscount()">
                <option value="Cash">Cash</option>
                <option value="Credit Card">Credit Card</option>
                <option value="Debit Card">Debit Card</option>
                <option value="Bank Transfer">Bank Transfer</option>
              </select>
            </div>
          </div>

          <div class="form-row-two">
            <div class="form-group">
              <label for="discountPercent">Discount (%)</label>
              <input type="number" name="discountPercent" id="discountPercent" class="form-input" value="0.00" readonly />
            </div>
          </div>

          <h3>Add Product</h3>
          <div class="form-row" style="gap: 12px; flex-wrap: wrap;">
            <select id="productSelect" class="form-input select2" style="min-width: 200px;">
              <option value="">-- Select Product --</option>
              <% for (Product p : products) { %>
                <option value="<%= p.getItemId() %>" data-price="<%= p.getPrice() %>">
                  <%= p.getName() %> (Rs. <%= p.getPrice() %>)
                </option>
              <% } %>
            </select>

            <input type="number" id="quantityInput" class="form-input" placeholder="Qty" min="1" value="1" />
            <input type="number" id="unitPriceInput" class="form-input" placeholder="Unit Price" readonly />
            <button type="button" class="btn btn-outline btn-sm" onclick="calculateSubtotal()">Calculate</button>
            <input type="text" id="subtotalOutput" class="form-input" placeholder="Subtotal" readonly />
            <input type="text" id="finalOutput" class="form-input" placeholder="Final Price (after discount)" readonly />
            <button type="button" class="btn btn-primary btn-sm" onclick="addProduct()">Add to Table</button>
          </div>

          <h3 style="margin-top: 24px;">Product List</h3>
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
            <tbody id="productTableBody">
              <!-- dynamic rows go here -->
            </tbody>
          </table>

          <div style="margin-top: 12px; text-align: right;">
            <strong>Grand Total: <span id="grandTotal">Rs. 0.00</span></strong>
          </div>

          <div class="form-row" style="margin-top: 30px;">
            <button type="submit" class="btn btn-primary">
              <i class="fas fa-check-circle"></i> Create Bill
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>

<script>
  $(document).ready(function () {
    $('.select2').select2();
    setDiscount();

    $('#productSelect').on('change', function () {
      const selected = $('#productSelect option:selected');
      const price = selected.attr('data-price');
      $('#unitPriceInput').val(price ? parseFloat(price).toFixed(2) : '');
      clearTotals();
    });
  });

  function setDiscount() {
    const method = document.getElementById("paymentMethod").value;
    const discountMap = {
      "Cash": 3,
      "Credit Card": 15,
      "Debit Card": 7,
      "Bank Transfer": 0
    };
    document.getElementById("discountPercent").value = discountMap[method] || 0;
    recalculateGrandTotal();
  }

  function calculateSubtotal() {
    const quantity = parseInt($('#quantityInput').val());
    const unitPrice = parseFloat($('#unitPriceInput').val());
    const discountPercent = parseFloat($('#discountPercent').val());

    if (isNaN(quantity) || quantity <= 0 || isNaN(unitPrice)) {
      alert("Please enter valid product and quantity.");
      return;
    }

    const subtotal = unitPrice * quantity;
    const discountAmount = subtotal * (discountPercent / 100);
    const finalTotal = subtotal - discountAmount;

    $('#subtotalOutput').val("Rs. " + subtotal.toFixed(2));
    $('#finalOutput').val("Rs. " + finalTotal.toFixed(2));
  }

  function addProduct() {
    const productSelect = document.getElementById("productSelect");
    const productId = productSelect.value;
    const productText = $('#productSelect option:selected').text();
    const quantity = parseInt(document.getElementById("quantityInput").value);
    const unitPrice = parseFloat(document.getElementById("unitPriceInput").value);
    const discountPercent = parseFloat(document.getElementById("discountPercent").value);

    if (!productId || isNaN(quantity) || isNaN(unitPrice)) {
      alert("Please calculate subtotal first.");
      return;
    }

    const subtotal = unitPrice * quantity;
    const discountAmount = subtotal * (discountPercent / 100);
    const finalTotal = subtotal - discountAmount;

    const row = document.createElement("tr");
    row.innerHTML = `
      <td>
        ${productText}
        <input type="hidden" name="productId[]" value="${productId}">
      </td>
      <td>
        ${quantity}
        <input type="hidden" name="quantity[]" value="${quantity}">
      </td>
      <td>
        Rs. ${unitPrice.toFixed(2)}
        <input type="hidden" name="unitPrice[]" value="${unitPrice}">
      </td>
      <td>Rs. ${subtotal.toFixed(2)}</td>
      <td>${discountPercent.toFixed(2)}%</td>
      <td class="item-total" data-amount="${finalTotal}">Rs. ${finalTotal.toFixed(2)}</td>
      <td>
        <button type="button" class="btn btn-outline btn-sm" onclick="removeRow(this)">
          <i class="fas fa-trash"></i> Remove
        </button>
      </td>
    `;

    document.getElementById("productTableBody").appendChild(row);

    $('#productSelect').val('').trigger('change');
    $('#quantityInput').val(1);
    $('#unitPriceInput').val('');
    clearTotals();
    recalculateGrandTotal();
  }

  function clearTotals() {
    $('#subtotalOutput').val('');
    $('#finalOutput').val('');
  }

  function removeRow(btn) {
    btn.closest('tr').remove();
    recalculateGrandTotal();
  }

  function recalculateGrandTotal() {
    let total = 0;
    document.querySelectorAll('.item-total').forEach(cell => {
      const amount = parseFloat(cell.getAttribute("data-amount"));
      if (!isNaN(amount)) {
        total += amount;
      }
    });
    document.getElementById("grandTotal").innerText = "Rs. " + total.toFixed(2);
  }

  function validateBeforeSubmit() {
    const productCount = document.querySelectorAll('input[name="productId[]"]').length;
    if (productCount === 0) {
      alert("Please add at least one product before submitting.");
      return false;
    }
    return true;
  }
</script>

</body>
</html>


