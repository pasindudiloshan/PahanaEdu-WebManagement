package com.pahanaedu.controller;

import com.pahanaedu.dao.BillDao;
import com.pahanaedu.model.Bill;
import com.pahanaedu.model.BillItem;
import com.pahanaedu.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

@WebServlet("/addBill")
public class AddBillServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Get parameters from request
            String accountNumber = request.getParameter("accountNumber");
            String paymentMethod = request.getParameter("paymentMethod");
            String discountStr = request.getParameter("discountPercent");

            String[] productIds = request.getParameterValues("productId[]");
            String[] quantities = request.getParameterValues("quantity[]");
            String[] unitPrices = request.getParameterValues("unitPrice[]");

            // Validation
            if (accountNumber == null || paymentMethod == null || discountStr == null ||
                productIds == null || quantities == null || unitPrices == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing billing data.");
                return;
            }

            double discountPercent = Double.parseDouble(discountStr);
            List<BillItem> billItems = new ArrayList<>();
            double totalAmount = 0.0;

            for (int i = 0; i < productIds.length; i++) {
                if (productIds[i] == null || productIds[i].trim().isEmpty()) continue;

                int qty = Integer.parseInt(quantities[i]);
                double price = Double.parseDouble(unitPrices[i]);
                double subtotal = qty * price;
                totalAmount += subtotal;

                BillItem item = new BillItem();
                item.setProductId(productIds[i]);
                item.setQuantity(qty);
                item.setUnitPrice(price);
                billItems.add(item);
            }

            double finalAmount = totalAmount - (totalAmount * discountPercent / 100.0);

            // Create bill object
            Bill bill = new Bill();
            bill.setAccountNumber(accountNumber);
            bill.setPaymentMethod(paymentMethod);
            bill.setDiscountPercent(discountPercent);
            bill.setTotalAmount(totalAmount);
            bill.setFinalAmount(finalAmount);
            bill.setBillingDate(new java.sql.Date(System.currentTimeMillis()));

            try (Connection conn = DBUtil.getConnection()) {
                BillDao dao = new BillDao(conn);

                // Insert main bill and get generated ID
                int billId = dao.insertBill(bill);

                // Set bill ID to each item and insert items
                for (BillItem item : billItems) {
                    item.setBillId(billId);
                }
                dao.insertBillItems(billItems);
            }

            // Optional: pass finalAmount to success page
            request.setAttribute("finalAmount", finalAmount);
            request.getRequestDispatcher("billinghistory.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server Error: " + e.getMessage());
        }
    }
}
