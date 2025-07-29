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

    public static boolean addProduct(Product product, InputStream imageStream) {
        String sql = "INSERT INTO products (item_id, item_name, description, price, quantity, image) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, product.getItemId());
            ps.setString(2, product.getName());
            ps.setString(3, product.getDescription());
            ps.setDouble(4, product.getPrice());
            ps.setInt(5, product.getQuantity());

            if (imageStream != null) {
                ps.setBlob(6, imageStream);
            } else {
                ps.setNull(6, java.sql.Types.BLOB);
            }

            int rowsInserted = ps.executeUpdate();
            System.out.println("DEBUG: Rows inserted: " + rowsInserted);
            return rowsInserted > 0;

        } catch (Exception e) {
            System.err.println("ERROR inserting product:");
            e.printStackTrace();
            return false;
        }
    }

    public static List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id, item_id, item_name, description, price, quantity FROM products ORDER BY id DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setItemId(rs.getString("item_id"));
                p.setName(rs.getString("item_name"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getDouble("price"));
                p.setQuantity(rs.getInt("quantity"));
                products.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }
}


