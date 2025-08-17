package com.pahanaedu.test;

import static org.junit.jupiter.api.Assertions.*;

import com.pahanaedu.dao.CustomerDao;
import com.pahanaedu.model.Customer;

import org.junit.jupiter.api.*;

import java.sql.Date;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CustomerDaoTest {

    private static final String TEST_ACCOUNT = "PEB-ACC-099";
    private static final String TEMP_ACCOUNT = "PEB-ACC-081";

    @BeforeAll
    public void setup() {
        Customer customer = new Customer();
        customer.setAccountNumber(TEST_ACCOUNT);
        customer.setFullName("JUnit Test Customer");
        customer.setPhone("1234567890");
        customer.setEmail("testcustomer@example.com");
        customer.setAddress("123 Test St");
        customer.setCity("TestCity");
        customer.setPostalCode("10000");
        customer.setImage(new byte[]{1, 2, 3});
        customer.setRegistrationDate(new Date(System.currentTimeMillis()));

        boolean added = CustomerDao.addCustomer(customer);
        assertTrue(added, "Customer should be added in setup");
    }

    @AfterAll
    public void cleanup() {
        CustomerDao.deleteCustomer(TEST_ACCOUNT);
        CustomerDao.deleteCustomer(TEMP_ACCOUNT);
    }

    // 1️⃣ Add Customer Test
    @Test
    public void testAddCustomer() {
        Customer temp = new Customer();
        temp.setAccountNumber(TEMP_ACCOUNT);
        temp.setFullName("Temp Customer");
        temp.setPhone("0000000000");
        temp.setEmail("temp@example.com");
        temp.setAddress("Temp Addr");
        temp.setCity("TempCity");
        temp.setPostalCode("00000");
        temp.setImage(new byte[]{4, 5, 6});
        temp.setRegistrationDate(new Date(System.currentTimeMillis()));

        boolean added = CustomerDao.addCustomer(temp);
        assertTrue(added, "Temp customer should be added");

        // Verify added
        Customer retrieved = CustomerDao.getCustomerByAccount(TEMP_ACCOUNT);
        assertNotNull(retrieved, "Customer should exist after add");
        assertEquals("Temp Customer", retrieved.getFullName());
    }

    // 2️⃣ Update Customer Test (with image)
    @Test
    public void testUpdateCustomerWithImage() {
        Customer customer = CustomerDao.getCustomerByAccount(TEST_ACCOUNT);
        assertNotNull(customer, "Customer must exist before update");

        customer.setFullName("Updated Customer");
        customer.setCity("UpdatedCity");

        boolean updated = CustomerDao.updateCustomer(customer);
        assertTrue(updated, "Customer should be updated");

        Customer updatedCustomer = CustomerDao.getCustomerByAccount(TEST_ACCOUNT);
        assertEquals("Updated Customer", updatedCustomer.getFullName());
        assertEquals("UpdatedCity", updatedCustomer.getCity());
    }

    // 3️⃣ Update Customer Test (without changing image)
    @Test
    public void testUpdateCustomerWithoutImage() {
        Customer customer = CustomerDao.getCustomerByAccount(TEST_ACCOUNT);
        assertNotNull(customer);

        byte[] originalImage = customer.getImage();
        customer.setFullName("No Image Update");
        customer.setImage(null); // do not change image

        boolean updated = CustomerDao.updateCustomer(customer);
        assertTrue(updated);

        Customer updatedCustomer = CustomerDao.getCustomerByAccount(TEST_ACCOUNT);
        assertEquals("No Image Update", updatedCustomer.getFullName());
        assertArrayEquals(originalImage, updatedCustomer.getImage(), "Image should remain unchanged");
    }

    // 4️⃣ Delete Customer Test
    @Test
    public void testDeleteCustomer() {
        // Ensure temp account does not already exist
        CustomerDao.deleteCustomer(TEMP_ACCOUNT);

        Customer temp = new Customer();
        temp.setAccountNumber(TEMP_ACCOUNT);
        temp.setFullName("Temp Customer Delete");
        temp.setPhone("0000000000");
        temp.setEmail("temp" + System.currentTimeMillis() + "@example.com"); // unique
        temp.setAddress("Temp Addr");
        temp.setCity("TempCity");
        temp.setPostalCode("00000");
        temp.setImage(new byte[]{7, 8, 9});
        temp.setRegistrationDate(new Date(System.currentTimeMillis()));

        boolean added = CustomerDao.addCustomer(temp);
        assertTrue(added, "Temp customer should be added for deletion");

        boolean deleted = CustomerDao.deleteCustomer(TEMP_ACCOUNT);
        assertTrue(deleted, "Temp customer should be deleted");

        Customer deletedCustomer = CustomerDao.getCustomerByAccount(TEMP_ACCOUNT);
        assertNull(deletedCustomer, "Deleted customer should not be found");
    }

    // 5️⃣ Delete Non-Existent Customer Test
    @Test
    public void testDeleteNonExistentCustomer() {
        boolean deleted = CustomerDao.deleteCustomer("NON-EXISTENT");
        assertFalse(deleted, "Deleting non-existent customer should return false");
    }

    // 6️⃣ Get Customer by Account Test
    @Test
    public void testGetCustomerByAccount() {
        Customer customer = CustomerDao.getCustomerByAccount(TEST_ACCOUNT);
        assertNotNull(customer, "Customer should be found");
        assertEquals("JUnit Test Customer", customer.getFullName());
    }

    // 7️⃣ Get All Customers Test
    @Test
    public void testGetAllCustomers() {
        List<Customer> customers = CustomerDao.getAllCustomers();
        assertNotNull(customers, "Customer list should not be null");
        assertTrue(customers.size() > 0, "There should be at least one customer");
    }

    // 8️⃣ Get Customer Count Test
    @Test
    public void testGetCustomerCount() {
        int count = CustomerDao.getCustomerCount();
        assertTrue(count >= 0, "Customer count should be non-negative");
    }

    // 9️⃣ Email Exists Test
    @Test
    public void testEmailExists() {
        assertTrue(CustomerDao.emailExists("testcustomer@example.com"));
        assertFalse(CustomerDao.emailExists("nonexistent@example.com"));
    }

    // 🔟 Email Exists For Other Test
    @Test
    public void testEmailExistsForOther() {
        assertFalse(CustomerDao.emailExistsForOther(TEST_ACCOUNT, "testcustomer@example.com")); // same account
        assertFalse(CustomerDao.emailExistsForOther(TEST_ACCOUNT, "nonexistent@example.com")); // different email
    }
}

