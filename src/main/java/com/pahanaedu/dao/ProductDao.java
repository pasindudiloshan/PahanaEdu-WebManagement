package com.pahanaedu.dao;

import com.pahanaedu.model.Product;
import com.pahanaedu.util.DBUtil;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {

    // Add product with image
    public static boolean addProduct(Product product, InputStream imageStream) {
        String sql = "INSERT INTO products (item_id, item_name, category, description, price, quantity, image) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, product.getItemId());
            ps.setString(2, product.getName());
            ps.setString(3, product.getCategory());
            ps.setString(4, product.getDescription());
            ps.setDouble(5, product.getPrice());
            ps.setInt(6, product.getQuantity());
            ps.setBlob(7, imageStream);

            int rowsInserted = ps.executeUpdate();
            return rowsInserted > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Check if Item ID already exists
    public static boolean itemIdExists(String itemId) {
        String sql = "SELECT COUNT(*) FROM products WHERE item_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, itemId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get all products
    public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id, item_id, item_name, category, description, price, quantity, image FROM products ORDER BY id DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setItemId(rs.getString("item_id"));
                p.setName(rs.getString("item_name"));
                p.setCategory(rs.getString("category"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getDouble("price"));
                p.setQuantity(rs.getInt("quantity"));
                p.setImage(rs.getBytes("image"));
                products.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }

    // Get product by ID
    public static Product getProductById(int id) {
        String sql = "SELECT * FROM products WHERE id=?";
        Product product = null;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                product = new Product();
                product.setId(rs.getInt("id"));
                product.setItemId(rs.getString("item_id"));
                product.setName(rs.getString("item_name"));
                product.setCategory(rs.getString("category"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("price"));
                product.setQuantity(rs.getInt("quantity"));
                product.setImage(rs.getBytes("image"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return product;
    }

    // Update product (with optional image)
    public static boolean updateProduct(Product product, InputStream imageStream) {
        String sqlWithImage = "UPDATE products SET item_id = ?, item_name = ?, category = ?, description = ?, price = ?, quantity = ?, image = ? WHERE id = ?";
        String sqlWithoutImage = "UPDATE products SET item_id = ?, item_name = ?, category = ?, description = ?, price = ?, quantity = ? WHERE id = ?";

        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement pst;
            if (imageStream != null) {
                pst = conn.prepareStatement(sqlWithImage);
                pst.setString(1, product.getItemId());
                pst.setString(2, product.getName());
                pst.setString(3, product.getCategory());
                pst.setString(4, product.getDescription());
                pst.setDouble(5, product.getPrice());
                pst.setInt(6, product.getQuantity());
                pst.setBlob(7, imageStream);
                pst.setInt(8, product.getId());
            } else {
                pst = conn.prepareStatement(sqlWithoutImage);
                pst.setString(1, product.getItemId());
                pst.setString(2, product.getName());
                pst.setString(3, product.getCategory());
                pst.setString(4, product.getDescription());
                pst.setDouble(5, product.getPrice());
                pst.setInt(6, product.getQuantity());
                pst.setInt(7, product.getId());
            }

            int rows = pst.executeUpdate();
            return rows > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete product by ID
    public static boolean deleteProductById(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rowsDeleted = ps.executeUpdate();
            return rowsDeleted > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get product count
    public static int getProductCount() {
        String sql = "SELECT COUNT(*) FROM products";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}





