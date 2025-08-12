package com.pahanaedu.dao;

import com.pahanaedu.model.Bill;
import com.pahanaedu.model.BillItem;
import com.pahanaedu.util.DBUtil;

import java.sql.*;
import java.util.*;

public class BillDao {
    private Connection conn;

    public BillDao(Connection conn) {
        this.conn = conn;
    }

    // Insert Bill and return generated ID
    public int insertBill(Bill bill) throws SQLException {
        String sql = "INSERT INTO bills (account_number, billing_date, payment_method, final_amount) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, bill.getAccountNumber());
            ps.setTimestamp(2, new java.sql.Timestamp(bill.getBillingDate().getTime())); // keep date + time
            ps.setString(3, bill.getPaymentMethod());
            ps.setDouble(4, bill.getFinalAmount());
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
        String sql = "INSERT INTO bill_items (bill_id, product_id, quantity, unit_price, discount_amount, final_price) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, item.getBillId());
            ps.setString(2, item.getProductId());
            ps.setInt(3, item.getQuantity());
            ps.setDouble(4, item.getUnitPrice());
            ps.setDouble(5, item.getDiscountAmount());
            ps.setDouble(6, item.getFinalPrice());
            ps.executeUpdate();
        }
    }

    // Batch insert BillItems for efficiency
    public void insertBillItems(List<BillItem> items) throws SQLException {
        String sql = "INSERT INTO bill_items (bill_id, product_id, quantity, unit_price, discount_amount, final_price) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (BillItem item : items) {
                ps.setInt(1, item.getBillId());
                ps.setString(2, item.getProductId());
                ps.setInt(3, item.getQuantity());
                ps.setDouble(4, item.getUnitPrice());
                ps.setDouble(5, item.getDiscountAmount());
                ps.setDouble(6, item.getFinalPrice());
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
                    b.setBillingDate(rs.getTimestamp("billing_date"));
                    b.setPaymentMethod(rs.getString("payment_method"));
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
        String sql = "SELECT bi.id, bi.bill_id, bi.product_id, bi.quantity, bi.unit_price, bi.discount_amount, bi.final_price, p.id AS product_numeric_id " +
                     "FROM bill_items bi " +
                     "JOIN products p ON bi.product_id = p.item_id " +
                     "WHERE bi.bill_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, billId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    BillItem item = new BillItem();
                    item.setId(rs.getInt("id"));
                    item.setBillId(rs.getInt("bill_id"));
                    item.setProductId(rs.getString("product_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setUnitPrice(rs.getDouble("unit_price"));
                    item.setDiscountAmount(rs.getDouble("discount_amount"));
                    item.setFinalPrice(rs.getDouble("final_price"));
                    item.setProductNumericId(rs.getInt("product_numeric_id")); // Set numeric ID here

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
                b.setBillingDate(rs.getTimestamp("billing_date"));
                b.setPaymentMethod(rs.getString("payment_method"));
                b.setFinalAmount(rs.getDouble("final_amount"));
                bills.add(b);
            }
        }
        return bills;
    }

    // Get Bills filtered by account number and/or date
    public List<Bill> getFilteredBills(String accountNumber, java.util.Date billingDate) throws SQLException {
        List<Bill> bills = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM bills WHERE 1=1");
        List<Object> parameters = new ArrayList<>();

        if (accountNumber != null && !accountNumber.trim().isEmpty()) {
            sql.append(" AND account_number = ?");
            parameters.add(accountNumber.trim());
        }

        if (billingDate != null) {
            // Filter for specific date only (midnight to midnight)
            Timestamp start = new Timestamp(billingDate.getTime());
            Timestamp end = new Timestamp(billingDate.getTime() + (1000L * 60 * 60 * 24));
            sql.append(" AND billing_date >= ? AND billing_date < ?");
            parameters.add(start);
            parameters.add(end);
        }

        sql.append(" ORDER BY billing_date DESC");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < parameters.size(); i++) {
                ps.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Bill b = new Bill();
                    b.setId(rs.getInt("id"));
                    b.setAccountNumber(rs.getString("account_number"));
                    b.setBillingDate(rs.getTimestamp("billing_date"));
                    b.setPaymentMethod(rs.getString("payment_method"));
                    b.setFinalAmount(rs.getDouble("final_amount"));
                    bills.add(b);
                }
            }
        }

        return bills;
    }

    // Get total quantity for a list of bills
    public Map<Integer, Integer> getTotalQuantitiesForBills(List<Bill> bills) throws SQLException {
        Map<Integer, Integer> result = new HashMap<>();
        if (bills == null || bills.isEmpty()) return result;

        StringBuilder sql = new StringBuilder("SELECT bill_id, SUM(quantity) as totalQty FROM bill_items WHERE bill_id IN (");
        for (int i = 0; i < bills.size(); i++) {
            sql.append("?");
            if (i < bills.size() - 1) sql.append(",");
        }
        sql.append(") GROUP BY bill_id");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < bills.size(); i++) {
                ps.setInt(i + 1, bills.get(i).getId());
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.put(rs.getInt("bill_id"), rs.getInt("totalQty"));
                }
            }
        }
        return result;
    }

    // Reduce stock quantity after billing
    public void reduceProductStock(String productId, int quantityUsed) throws SQLException {
        String sql = "UPDATE products SET quantity = quantity - ? WHERE item_id = ? AND quantity >= ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantityUsed);
            ps.setString(2, productId);
            ps.setInt(3, quantityUsed);
            int updated = ps.executeUpdate();
            if (updated == 0) {
                throw new SQLException("Insufficient stock for product: " + productId);
            }
        }
    }

    // Get total sales (sum of bill final_amount)
    public double getTotalSales() throws SQLException {
        String sql = "SELECT SUM(final_amount) AS totalSales FROM bills";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble("totalSales");
            }
        }
        return 0.0;
    }

    // Get total order count
    public int getTotalOrderCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM bills";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    // Get recent bills with limit
    public List<Bill> getRecentBills(int limit) {
        List<Bill> recentBills = new ArrayList<>();
        String sql = "SELECT * FROM bills ORDER BY billing_date DESC LIMIT ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Bill bill = new Bill();
                    bill.setId(rs.getInt("id"));
                    bill.setAccountNumber(rs.getString("account_number"));
                    bill.setBillingDate(rs.getTimestamp("billing_date"));
                    bill.setFinalAmount(rs.getDouble("final_amount"));
                    bill.setPaymentMethod(rs.getString("payment_method"));
                    recentBills.add(bill);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recentBills;
    }
}

