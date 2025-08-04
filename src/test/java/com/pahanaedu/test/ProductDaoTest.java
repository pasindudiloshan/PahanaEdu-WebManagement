package com.pahanaedu.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.*;

import com.pahanaedu.dao.ProductDao;
import com.pahanaedu.model.Product;

@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Allows non-static @BeforeAll/@AfterAll
public class ProductDaoTest {

    private Product testProduct;

    @BeforeAll
    public void setup() {
        // Prepare a test product
        testProduct = new Product();
        testProduct.setItemId("TEST-ITEM-001");
        testProduct.setName("JUnit Test Product");
        testProduct.setCategory("Test Category");
        testProduct.setDescription("Description for JUnit test product");
        testProduct.setPrice(9.99);
        testProduct.setQuantity(10);

        // Dummy image data
        byte[] dummyImage = {1, 2, 3, 4, 5};
        InputStream imageStream = new ByteArrayInputStream(dummyImage);

        boolean added = ProductDao.addProduct(testProduct, imageStream);
        assertTrue(added, "Setup: Test product should be added");
        
        // Get the inserted product to fetch the generated ID
        List<Product> allProducts = ProductDao.getAllProducts();
        // Assuming last product is newest inserted
        testProduct = allProducts.get(0);
    }

    @AfterAll
    public void cleanup() {
        if (testProduct != null && testProduct.getId() > 0) {
            boolean deleted = ProductDao.deleteProductById(testProduct.getId());
            assertTrue(deleted, "Cleanup: Test product should be deleted");
        }
    }

    @Test
    public void testGetAllProducts() {
        List<Product> products = ProductDao.getAllProducts();
        assertNotNull(products, "Product list should not be null");
        assertTrue(products.size() > 0, "Product list should contain at least one product");
    }

    @Test
    public void testGetProductById() {
        Product p = ProductDao.getProductById(testProduct.getId());
        assertNotNull(p, "Product should be found by ID");
        assertEquals(testProduct.getItemId(), p.getItemId(), "Item IDs should match");
    }

    @Test
    public void testUpdateProduct() {
        testProduct.setName("Updated Product Name");
        testProduct.setCategory("Updated Category");
        testProduct.setDescription("Updated description");
        testProduct.setPrice(19.99);
        testProduct.setQuantity(20);

        // Use null to keep existing image
        boolean updated = ProductDao.updateProduct(testProduct, null);
        assertTrue(updated, "Product should be updated");

        // Verify changes
        Product updatedProduct = ProductDao.getProductById(testProduct.getId());
        assertEquals("Updated Product Name", updatedProduct.getName(), "Name should be updated");
        assertEquals("Updated Category", updatedProduct.getCategory(), "Category should be updated");
        assertEquals(19.99, updatedProduct.getPrice(), 0.01, "Price should be updated");
        assertEquals(20, updatedProduct.getQuantity(), "Quantity should be updated");
    }

    @Test
    public void testGetProductCount() {
        int count = ProductDao.getProductCount();
        assertTrue(count > 0, "Product count should be greater than zero");
    }

    @Test
    public void testDeleteProductById() {
        // Create a temporary product to delete
        Product tempProduct = new Product();
        tempProduct.setItemId("TEMP-ITEM-001");
        tempProduct.setName("Temp Product");
        tempProduct.setCategory("Temp Category");
        tempProduct.setDescription("Temporary product for delete test");
        tempProduct.setPrice(1.23);
        tempProduct.setQuantity(1);

        byte[] dummyImage = {9, 8, 7};
        InputStream imageStream = new ByteArrayInputStream(dummyImage);
        boolean added = ProductDao.addProduct(tempProduct, imageStream);
        assertTrue(added, "Temporary product should be added");

        // Get inserted product to find ID
        List<Product> products = ProductDao.getAllProducts();
        Product lastProduct = products.get(0);

        boolean deleted = ProductDao.deleteProductById(lastProduct.getId());
        assertTrue(deleted, "Temporary product should be deleted");

        // Confirm deletion
        Product deletedProduct = ProductDao.getProductById(lastProduct.getId());
        assertNull(deletedProduct, "Deleted product should no longer exist");
    }
}
