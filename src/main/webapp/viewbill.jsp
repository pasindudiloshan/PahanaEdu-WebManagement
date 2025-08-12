<%@ page import="com.pahanaedu.model.Bill" %>
<%@ page import="com.pahanaedu.model.BillItem" %>
<%@ page import="com.pahanaedu.model.Product" %>
<%@ page import="com.pahanaedu.dao.ProductDao" %>
<%@ page import="com.pahanaedu.dao.CustomerDao" %>
<%@ page import="com.pahanaedu.model.Customer" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    Bill bill = (Bill) request.getAttribute("bill");
    Customer customer = null;
    if (bill != null) {
        customer = CustomerDao.getCustomerByAccount(bill.getAccountNumber());
    }
    @SuppressWarnings("unchecked")
    List<BillItem> billItems = (List<BillItem>) request.getAttribute("billItems");

    double grandTotal = 0.0;
%>
<!DOCTYPE html>
<html>
<head>
    <title>Invoice - PahanaEdu</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" rel="stylesheet" />
    <link rel="stylesheet" href="css/invoice.css" />
</head>
<body>

<div class="header">
    <div class="logo">
        <img src="images/Colorpahanalogo.png" alt="PahanaEdu Logo" />
        <small><%= bill != null ? bill.getBillingDate() : "" %></small>
    </div>
    <div class="invoice-number">
        <%= bill != null ? "PEB-ORD-" + String.format("%03d", bill.getId()) : "" %>
    </div>
</div>

<div class="bill-to">
    <strong>BILL TO:</strong><br>
    Name: <%= (customer != null) ? customer.getFullName() : "Unknown Customer" %><br>
    Account: <%= bill != null ? bill.getAccountNumber() : "" %><br>
    Payment Method: <%= bill != null ? bill.getPaymentMethod() : "" %>
</div>

<table>
    <thead>
        <tr>
            <th>Image</th>
            <th>Product Name</th>
            <th>Product ID</th>
            <th>Qty</th>
            <th>Unit Price</th>
            <th>Discount</th>
            <th>Final Price</th>
        </tr>
    </thead>
    <tbody>
        <%
            if (billItems != null) {
                for (BillItem item : billItems) {
                    Product product = ProductDao.getProductById(item.getProductNumericId());
                    String productName = (product != null) ? product.getName() : "Unknown Product";
                    double unitPrice = (product != null) ? product.getPrice() : 0.0;
                    double subtotal = item.getQuantity() * item.getFinalPrice();
                    grandTotal += subtotal;
        %>
        <tr>
            <td>
                <img class="product-img" src="productImage?id=<%= item.getProductNumericId() %>"
                     onerror="this.onerror=null;this.src='images/default-product.png';" />
            </td>
            <td><%= productName %></td>
            <td><%= item.getProductId() %></td>
            <td><%= item.getQuantity() %></td>
            <td>Rs. <%= String.format("%.2f", unitPrice) %></td>
            <td>Rs. <%= String.format("%.2f", item.getDiscountAmount()) %></td>
            <td>Rs. <%= String.format("%.2f", item.getFinalPrice()) %></td>
        </tr>
        <%      }
            }
        %>
    </tbody>
</table>

<table class="totals">
    <tr>
        <td><strong>Grand Total:</strong></td>
        <td class="grand-total">Rs. <%= String.format("%.2f", grandTotal) %></td>
    </tr>
</table>

<div class="payment-info">
    <strong>PAYMENT INFORMATION:</strong><br>
    Bank: Example Bank<br>
    Account: 0000 0000 0000<br><br>
    Payment is due within 30 days from invoice date.
</div>

<div class="bottom-buttons">
    <button class="btn btn-back" onclick="window.location.href='billinghistory.jsp'">
        <i class="fas fa-arrow-left"></i> Back to Sales
    </button>
    <button class="btn btn-print" onclick="window.print()">
        <i class="fas fa-print"></i> Print Bill
    </button>
</div>

<div class="footer">
    <div>📞 +94 74 0666 500</div>
    <div>✉ pahanaeducation@gmail.com</div>
    <div>🌐 www.pahanaedu.com</div>
</div>

</body>
</html>