package com.pahanaedu.test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import org.junit.jupiter.api.*;

import com.pahanaedu.dao.BillDao;
import com.pahanaedu.model.Bill;
import com.pahanaedu.model.BillItem;
import com.pahanaedu.util.DBUtil;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BillDaoTest {

    private Connection conn;
    private BillDao billDao;
    private int insertedBillId;

    @BeforeAll
    public void setup() throws SQLException {
        conn = DBUtil.getConnection();
        conn.setAutoCommit(false);  // Manual commit control for testing
        billDao = new BillDao(conn);
    }

    @AfterAll
    public void cleanup() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.rollback();  // Rollback to keep DB clean after tests
            conn.close();
        }
    }

    @Test
    @Order(1)
    public void testInsertBillAndBillItems() throws SQLException {
        Bill bill = new Bill();
        bill.setAccountNumber("PEB-ACC-020");  // real customer
        bill.setBillingDate(new Date());
        bill.setPaymentMethod("Credit Card");
        bill.setFinalAmount(700.00);

        insertedBillId = billDao.insertBill(bill);
        assertTrue(insertedBillId > 0, "Bill ID should be generated");

        BillItem item1 = new BillItem();
        item1.setBillId(insertedBillId);
        item1.setProductId("PEB-ITEM-023");  // existing product
        item1.setQuantity(2);
        item1.setUnitPrice(350.00);
        item1.setDiscountAmount(0);
        item1.setFinalPrice(700.00);

        billDao.insertBillItem(item1);

        conn.commit();
    }

    @Test
    @Order(2)
    public void testGetBillById() throws SQLException {
        Bill bill = billDao.getBillById(insertedBillId);
        assertNotNull(bill, "Bill should be retrieved by ID");
        assertEquals("PEB-ACC-020", bill.getAccountNumber(), "Account number should match");
    }

    @Test
    @Order(3)
    public void testGetBillItems() throws SQLException {
        List<BillItem> items = billDao.getBillItems(insertedBillId);
        assertNotNull(items);
        assertTrue(items.size() > 0);

        BillItem item = items.get(0);
        assertEquals(insertedBillId, item.getBillId());
        assertEquals("PEB-ITEM-023", item.getProductId());
    }

    @Test
    @Order(4)
    public void testGetAllBills() throws SQLException {
        List<Bill> bills = billDao.getAllBills();
        assertNotNull(bills);
        assertTrue(bills.size() > 0);

        boolean found = bills.stream().anyMatch(b -> b.getId() == insertedBillId);
        assertTrue(found);
    }

    @Test
    @Order(5)
    public void testInsertBillItemsBatch() throws SQLException {
        BillItem item1 = new BillItem();
        item1.setBillId(insertedBillId);
        item1.setProductId("PEB-ITEM-023");
        item1.setQuantity(1);
        item1.setUnitPrice(350.00);
        item1.setDiscountAmount(0);
        item1.setFinalPrice(350.00);

        BillItem item2 = new BillItem();
        item2.setBillId(insertedBillId);
        item2.setProductId("PEB-ITEM-023");
        item2.setQuantity(3);
        item2.setUnitPrice(350.00);
        item2.setDiscountAmount(0);
        item2.setFinalPrice(1050.00);

        List<BillItem> items = List.of(item1, item2);
        billDao.insertBillItems(items);

        List<BillItem> fetchedItems = billDao.getBillItems(insertedBillId);
        assertNotNull(fetchedItems);
        assertTrue(fetchedItems.size() >= 3);

        assertTrue(fetchedItems.stream().anyMatch(i -> i.getQuantity() == 1 && i.getFinalPrice() == 350.00));
        assertTrue(fetchedItems.stream().anyMatch(i -> i.getQuantity() == 3 && i.getFinalPrice() == 1050.00));
    }

    // --- New tests for remaining methods ---

    @Test
    @Order(6)
    public void testGetFilteredBills() throws SQLException {
        // Filter by accountNumber only
        List<Bill> billsByAccount = billDao.getFilteredBills("PEB-ACC-020", null);
        assertNotNull(billsByAccount);
        assertTrue(billsByAccount.stream().allMatch(b -> b.getAccountNumber().equals("PEB-ACC-020")));

        // Filter by billingDate only (use inserted bill date)
        Bill insertedBill = billDao.getBillById(insertedBillId);
        List<Bill> billsByDate = billDao.getFilteredBills(null, insertedBill.getBillingDate());
        assertNotNull(billsByDate);
        assertTrue(billsByDate.size() > 0);

        // Filter by both
        List<Bill> billsByBoth = billDao.getFilteredBills("PEB-ACC-020", insertedBill.getBillingDate());
        assertNotNull(billsByBoth);
        assertTrue(billsByBoth.size() > 0);
    }

    @Test
    @Order(7)
    public void testGetTotalQuantitiesForBills() throws SQLException {
        List<Bill> bills = billDao.getAllBills();
        Map<Integer, Integer> totalQuantities = billDao.getTotalQuantitiesForBills(bills);
        assertNotNull(totalQuantities);
        assertTrue(totalQuantities.containsKey(insertedBillId));
        assertTrue(totalQuantities.get(insertedBillId) >= 3); // at least 3 items inserted previously
    }

    @Test
    @Order(8)
    public void testReduceProductStock() throws SQLException {
        // Get current quantity before reduction
        String productId = "PEB-ITEM-023";
        int quantityToReduce = 1;

        // We must query current stock first (using plain query)
        int beforeQty = getProductQuantity(productId);

        // Reduce stock by quantityToReduce
        billDao.reduceProductStock(productId, quantityToReduce);

        int afterQty = getProductQuantity(productId);

        assertEquals(beforeQty - quantityToReduce, afterQty);

        // Rollback stock change manually so other tests not affected
        rollbackProductQuantity(productId, quantityToReduce);
    }

    @Test
    @Order(9)
    public void testGetTotalSales() throws SQLException {
        double totalSales = billDao.getTotalSales();
        assertTrue(totalSales >= 700.0);
    }

    @Test
    @Order(10)
    public void testGetTotalOrderCount() {
        int count = billDao.getTotalOrderCount();
        assertTrue(count > 0);
    }

    @Test
    @Order(11)
    public void testGetRecentBills() {
        List<Bill> recentBills = billDao.getRecentBills(5);
        assertNotNull(recentBills);
        assertTrue(recentBills.size() <= 5);
    }

    // --- Helper methods for testReduceProductStock ---

    private int getProductQuantity(String productId) throws SQLException {
        String sql = "SELECT quantity FROM products WHERE item_id = ?";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setString(1, productId);
            try (var rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("quantity");
                }
            }
        }
        throw new SQLException("Product not found: " + productId);
    }

    private void rollbackProductQuantity(String productId, int qty) throws SQLException {
        String sql = "UPDATE products SET quantity = quantity + ? WHERE item_id = ?";
        try (var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setString(2, productId);
            ps.executeUpdate();
        }
    }
}

