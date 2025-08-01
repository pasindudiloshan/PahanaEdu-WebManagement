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
    // Redirect unauthorized users
    User loggedUser = (User) session.getAttribute("user");
    if (loggedUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    if (!"Admin".equalsIgnoreCase(loggedUser.getRole())) {
        response.sendRedirect("index.jsp");
        return;
    }

    // Fetch data needed for the form
    List<Customer> customers = CustomerDao.getAllCustomers();
    List<Product> products = ProductDao.getAllProducts();
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Create New Bill</title>

    <!-- CSS Stylesheets -->
    <link rel="stylesheet" href="css/product-management.css" />
    <link rel="stylesheet" href="css/sidebar-header.css" />
    <link rel="stylesheet" href="css/bill.css" />
    <link rel="icon" type="image/x-icon" href="images/favicon.png" />
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />

    <!-- External Libraries -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js" defer></script>
    <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js" defer></script>

    <!-- Custom JS -->
    <script src="js/bill.js" defer></script>
</head>

<body>
    <div class="container">
        <%@ include file="sidebar.jsp" %>
        <%@ include file="header.jsp" %>

        <main class="main-content">
            <header class="page-title">
                <h1>Create New Bill</h1>
                <nav class="action-buttons">
                    <a href="billinghistory.jsp" class="btn btn-outline">
                        <i class="fas fa-arrow-left"></i> Back
                    </a>
                </nav>
            </header>

            <section class="table-card">
                <header class="card-title">
                    <h3><i class="fas fa-file-invoice-dollar"></i> Bill Details</h3>
                </header>

                <form id="billForm" action="addBill" method="post" onsubmit="return validateBeforeSubmit()" style="padding: 24px;">
                    <!-- Customer Selection -->
                    <div class="form-group">
                        <label for="accountNumber">Customer <span class="required">*</span></label>
                        <select name="accountNumber" id="accountNumber" class="form-input select2" required>
                            <option value="">-- Select Customer --</option>
                            <% for (Customer c : customers) { %>
                                <option value="<%= c.getAccountNumber() %>">
                                    <%= c.getFullName() %> (<%= c.getAccountNumber() %>)
                                </option>
                            <% } %>
                        </select>
                    </div>

                    <!-- Add Product Row -->
                    <fieldset class="form-row form-row-wrap" style="gap: 12px;">
                       <label for="addproduct">Add Product <span style="color:red">*</span></label>

                        <select id="productSelect" class="form-input select2" style="min-width: 200px;">
                            <option value="">-- Select Product --</option>
                            <% for (Product p : products) { %>
                                <option value="<%= p.getItemId() %>" 
                                        data-price="<%= p.getPrice() %>" 
                                        data-stock="<%= p.getQuantity() %>">
                                    <%= p.getName() %> (Rs. <%= p.getPrice() %>, Stock: <%= p.getQuantity() %>)
                                </option>
                            <% } %>
                        </select>

                        <input type="number" id="quantityInput" class="form-input" placeholder="Qty" min="1" value="1" />

                        <input type="number" id="unitPriceInput" class="form-input" placeholder="Unit Price" readonly />

                        <select name="paymentMethod" id="paymentMethod" class="form-input" required onchange="setDiscount()" style="min-width: 150px;">
                            <option value="Cash">Cash</option>
                            <option value="Credit Card">Credit Card</option>
                            <option value="Debit Card">Debit Card</option>
                            <option value="Bank Transfer">Bank Transfer</option>
                        </select>

                        <input type="number" name="discountPercent" id="discountPercent" class="form-input" value="0.00" readonly placeholder="Discount %" />

                        <button type="button" class="btn btn-outline btn-sm" onclick="calculateSubtotal()">Calculate</button>

                        <input type="text" id="subtotalOutput" class="form-input" placeholder="Subtotal" readonly />

                        <input type="text" id="finalOutput" class="form-input" placeholder="Final Price (after discount)" readonly />

                        <button type="button" class="btn btn-primary btn-sm" onclick="addProduct()">Add to Table</button>
                    </fieldset>

                    <!-- Product List Table -->
                    <h3 style="margin-top: 24px;">Product List</h3>
                    <table class="data-table" id="productTable" aria-label="Selected Products">
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
                            <!-- JS will append rows here -->
                        </tbody>
                    </table>

                    <!-- Grand Total -->
                    <div class="text-right" style="margin-top: 12px;">
                        <strong>Grand Total: <span id="grandTotal">Rs. 0.00</span></strong>
                    </div>

                    <!-- Submit Button -->
                    <div class="form-row" style="margin-top: 30px;">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-check-circle"></i> Create Bill
                        </button>
                    </div>
                </form>
            </section>
        </main>
    </div>
</body>
</html>


