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
import java.sql.SQLException;
import java.util.*;

@WebServlet("/addBill")
public class AddBillServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Connection conn = null;

        try {
            String accountNumber = request.getParameter("accountNumber");
            String paymentMethod = request.getParameter("paymentMethod");

            String[] productIds = request.getParameterValues("productId[]");
            String[] quantities = request.getParameterValues("quantity[]");
            String[] unitPrices = request.getParameterValues("unitPrice[]");

            if (accountNumber == null || paymentMethod == null ||
                productIds == null || quantities == null || unitPrices == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing billing data.");
                return;
            }

            List<BillItem> billItems = new ArrayList<>();
            double totalAmount = 0.0;

            for (int i = 0; i < productIds.length; i++) {
                String productId = safe(productIds[i]);
                if (productId.isEmpty()) continue;

                int qty = parseIntSafe(quantities[i], 0);
                double price = parseDoubleSafe(unitPrices[i], 0.0);
                if (qty <= 0 || price < 0.0) continue;

                double subtotal = qty * price;
                totalAmount += subtotal;

                BillItem item = new BillItem();
                item.setProductId(productId);
                item.setQuantity(qty);
                item.setUnitPrice(price);
                billItems.add(item);
            }

            if (billItems.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No valid items added.");
                return;
            }

            Bill bill = new Bill();
            bill.setAccountNumber(accountNumber);
            bill.setPaymentMethod(paymentMethod);
            bill.setFinalAmount(totalAmount);
            bill.setBillingDate(new java.sql.Date(System.currentTimeMillis()));

            conn = DBUtil.getConnection();
            conn.setAutoCommit(false); // Start transaction

            BillDao dao = new BillDao(conn);
            int billId = dao.insertBill(bill);

            // First reduce stock per item
            for (BillItem item : billItems) {
                dao.reduceProductStock(item.getProductId(), item.getQuantity());
                item.setBillId(billId); // set bill ID after stock deduction
            }

            // Then insert all items
            dao.insertBillItems(billItems);

            conn.commit(); // all good

            request.setAttribute("finalAmount", totalAmount);
            request.getRequestDispatcher("billinghistory.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();

            if (conn != null) {
                try {
                    conn.rollback(); // rollback everything if any failure
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Transaction failed: " + e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // reset autocommit
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String safe(String input) {
        return input == null ? "" : input.trim();
    }

    private int parseIntSafe(String input, int defaultValue) {
        try {
            return Integer.parseInt(safe(input));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private double parseDoubleSafe(String input, double defaultValue) {
        try {
            return Double.parseDouble(safe(input));
        } catch (Exception e) {
            return defaultValue;
        }
    }
}


