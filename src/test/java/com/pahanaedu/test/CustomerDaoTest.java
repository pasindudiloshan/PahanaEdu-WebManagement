package com.pahanaedu.test;

import static org.junit.jupiter.api.Assertions.*;

import com.pahanaedu.dao.CustomerDao;
import com.pahanaedu.model.Customer;

import org.junit.jupiter.api.*;

import java.sql.Date;
import java.util.List;

public class CustomerDaoTest {

    private static final String TEST_ACCOUNT = "PEB-ACC-099";
    private static final String TEMP_ACCOUNT = "PEB-ACC-081";

    @BeforeAll
    public static void setup() {
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
    public static void cleanup() {
        CustomerDao.deleteCustomer(TEST_ACCOUNT);
        CustomerDao.deleteCustomer(TEMP_ACCOUNT); // clean if temp used
    }

    @Test
    public void testGetCustomerByAccount() {
        Customer customer = CustomerDao.getCustomerByAccount(TEST_ACCOUNT);
        assertNotNull(customer, "Customer should be found");
        assertEquals("JUnit Test Customer", customer.getFullName());
    }

    @Test
    public void testUpdateCustomer() {
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

    @Test
    public void testGetAllCustomers() {
        List<Customer> customers = CustomerDao.getAllCustomers();
        assertNotNull(customers, "Customer list should not be null");
        assertTrue(customers.size() > 0, "There should be at least one customer");
    }

    @Test
    public void testGetCustomerCount() {
        int count = CustomerDao.getCustomerCount();
        assertTrue(count >= 0, "Customer count should be non-negative");
    }

    @Test
    public void testDeleteCustomer() {
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

        boolean deleted = CustomerDao.deleteCustomer(TEMP_ACCOUNT);
        assertTrue(deleted, "Temp customer should be deleted");

        Customer deletedCustomer = CustomerDao.getCustomerByAccount(TEMP_ACCOUNT);
        assertNull(deletedCustomer, "Deleted customer should not be found");
    }
}
