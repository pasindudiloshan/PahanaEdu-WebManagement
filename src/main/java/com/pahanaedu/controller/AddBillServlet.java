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
            String[] discountAmounts = request.getParameterValues("discountAmount[]");

            String finalAmountStr = request.getParameter("finalAmount");
            double finalAmount = parseDoubleSafe(finalAmountStr, 0.0);

            // Basic validation
            if (accountNumber == null || paymentMethod == null ||
                productIds == null || quantities == null || unitPrices == null ||
                discountAmounts == null || finalAmount <= 0.0) {
                response.sendRedirect("createbill.jsp?status=invalid");
                return;
            }

            // Deduplicate and aggregate BillItems if needed
            Map<String, BillItem> uniqueItemsMap = new LinkedHashMap<>();

            for (int i = 0; i < productIds.length; i++) {
                String productId = safe(productIds[i]);
                if (productId.isEmpty()) continue;

                int qty = parseIntSafe(quantities[i], 0);
                double unitPrice = parseDoubleSafe(unitPrices[i], 0.0);
                double discountAmount = parseDoubleSafe(discountAmounts[i], 0.0);

                if (qty <= 0 || unitPrice < 0.0) continue;

                if (uniqueItemsMap.containsKey(productId)) {
                    BillItem existingItem = uniqueItemsMap.get(productId);
                    existingItem.setQuantity(existingItem.getQuantity() + qty);
                    existingItem.setDiscountAmount(existingItem.getDiscountAmount() + discountAmount);
                    existingItem.setFinalPrice(existingItem.getFinalPrice() + (unitPrice * qty - discountAmount));
                } else {
                    BillItem item = new BillItem();
                    item.setProductId(productId);
                    item.setQuantity(qty);
                    item.setUnitPrice(unitPrice);
                    item.setDiscountAmount(discountAmount);
                    item.setFinalPrice(unitPrice * qty - discountAmount);
                    uniqueItemsMap.put(productId, item);
                }
            }

            List<BillItem> billItems = new ArrayList<>(uniqueItemsMap.values());

            if (billItems.isEmpty()) {
                response.sendRedirect("createbill.jsp?status=invalid");
                return;
            }

            // DB transaction for inserting Bill and BillItems
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            BillDao dao = new BillDao(conn);

            Bill bill = new Bill();
            bill.setAccountNumber(accountNumber);
            bill.setPaymentMethod(paymentMethod);
            bill.setFinalAmount(finalAmount);
            bill.setBillingDate(new java.util.Date()); // current timestamp

            int billId = dao.insertBill(bill);

            // Reduce stock & assign billId
            for (BillItem item : billItems) {
                dao.reduceProductStock(item.getProductId(), item.getQuantity());
                item.setBillId(billId);
            }

            // Insert bill items
            dao.insertBillItems(billItems);

            conn.commit();

            // Redirect with success
            response.sendRedirect("createbill.jsp?status=success");

        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            response.sendRedirect("createbill.jsp?status=failed");
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Utility helpers
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
