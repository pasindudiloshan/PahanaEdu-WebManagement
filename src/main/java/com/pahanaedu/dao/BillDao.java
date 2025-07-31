package com.pahanaedu.dao;

import com.pahanaedu.model.Bill;
import com.pahanaedu.model.BillItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BillDao {
    private Connection conn;

    public BillDao(Connection conn) {
        this.conn = conn;
    }

    // Insert Bill and return generated ID
    public int insertBill(Bill bill) throws SQLException {
        String sql = "INSERT INTO bills (account_number, billing_date, payment_method, discount_percent, total_amount, final_amount) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, bill.getAccountNumber());
            ps.setDate(2, new java.sql.Date(bill.getBillingDate().getTime()));
            ps.setString(3, bill.getPaymentMethod());
            ps.setDouble(4, bill.getDiscountPercent());
            ps.setDouble(5, bill.getTotalAmount());
            ps.setDouble(6, bill.getFinalAmount());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return -1;
    }

    // Insert single BillItem
    public void insertBillItem(BillItem item) throws SQLException {
        String sql = "INSERT INTO bill_items (bill_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, item.getBillId());
            ps.setString(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.setDouble(4, item.getUnitPrice());
            ps.executeUpdate();
        }
    }

    // Batch insert BillItems for efficiency
    public void insertBillItems(List<BillItem> items) throws SQLException {
        String sql = "INSERT INTO bill_items (bill_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (BillItem item : items) {
                ps.setInt(1, item.getBillId());
                ps.setString(2, item.getProductId());
                ps.setInt(3, item.getQuantity());
                ps.setDouble(4, item.getUnitPrice());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    // Get Bill by ID
    public Bill getBillById(int id) throws SQLException {
        String sql = "SELECT * FROM bills WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Bill b = new Bill();
                    b.setId(rs.getInt("id"));
                    b.setAccountNumber(rs.getString("account_number"));
                    b.setBillingDate(rs.getDate("billing_date"));
                    b.setPaymentMethod(rs.getString("payment_method"));
                    b.setDiscountPercent(rs.getDouble("discount_percent"));
                    b.setTotalAmount(rs.getDouble("total_amount"));
                    b.setFinalAmount(rs.getDouble("final_amount"));
                    return b;
                }
            }
        }
        return null;
    }

    // Get BillItems by bill ID
    public List<BillItem> getBillItems(int billId) throws SQLException {
        List<BillItem> items = new ArrayList<>();
        String sql = "SELECT * FROM bill_items WHERE bill_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, billId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BillItem item = new BillItem();
                    item.setId(rs.getInt("id"));
                    item.setBillId(rs.getInt("bill_id"));
                    item.setProductId(rs.getString("product_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setUnitPrice(rs.getDouble("unit_price"));
                    items.add(item);
                }
            }
        }
        return items;
    }

    // Get all Bills ordered by billing_date DESC
    public List<Bill> getAllBills() throws SQLException {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM bills ORDER BY billing_date DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Bill b = new Bill();
                b.setId(rs.getInt("id"));
                b.setAccountNumber(rs.getString("account_number"));
                b.setBillingDate(rs.getDate("billing_date"));
                b.setPaymentMethod(rs.getString("payment_method"));
                b.setDiscountPercent(rs.getDouble("discount_percent"));
                b.setTotalAmount(rs.getDouble("total_amount"));
                b.setFinalAmount(rs.getDouble("final_amount"));
                bills.add(b);
            }
        }
        return bills;
    }

    // Get Bills filtered by account number
    public List<Bill> getBillsByAccountNumber(String accountNumber) throws SQLException {
        List<Bill> bills = new ArrayList<>();
        String sql = "SELECT * FROM bills WHERE account_number = ? ORDER BY billing_date DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Bill b = new Bill();
                    b.setId(rs.getInt("id"));
                    b.setAccountNumber(rs.getString("account_number"));
                    b.setBillingDate(rs.getDate("billing_date"));
                    b.setPaymentMethod(rs.getString("payment_method"));
                    b.setDiscountPercent(rs.getDouble("discount_percent"));
                    b.setTotalAmount(rs.getDouble("total_amount"));
                    b.setFinalAmount(rs.getDouble("final_amount"));
                    bills.add(b);
                }
            }
        }
        return bills;
    }
    
    public List<Bill> getFilteredBills(String accountNumber, java.util.Date billingDate) throws SQLException {
        List<Bill> bills = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM bills WHERE 1=1");

        if (accountNumber != null && !accountNumber.trim().isEmpty()) {
            sql.append(" AND account_number LIKE ?");
        }
        if (billingDate != null) {
            sql.append(" AND billing_date >= ? AND billing_date < ?");
        }

        sql.append(" ORDER BY billing_date DESC");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int index = 1;

            if (accountNumber != null && !accountNumber.trim().isEmpty()) {
                ps.setString(index++, accountNumber.trim() + "%"); // ✅ Partial match
            }

            if (billingDate != null) {
                java.sql.Date start = new java.sql.Date(billingDate.getTime());
                java.sql.Date end = new java.sql.Date(billingDate.getTime() + (1000 * 60 * 60 * 24));
                ps.setDate(index++, start);
                ps.setDate(index++, end);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Bill b = new Bill();
                    b.setId(rs.getInt("id"));
                    b.setAccountNumber(rs.getString("account_number"));
                    b.setBillingDate(rs.getDate("billing_date"));
                    b.setPaymentMethod(rs.getString("payment_method"));
                    b.setDiscountPercent(rs.getDouble("discount_percent"));
                    b.setTotalAmount(rs.getDouble("total_amount"));
                    b.setFinalAmount(rs.getDouble("final_amount"));
                    bills.add(b);
                }
            }
        }

        return bills;
    }


}
