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
        String sql = "SELECT id, item_id, item_name, description, price, quantity, image FROM products ORDER BY id DESC";

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
                p.setImage(rs.getBytes("image"));
                products.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }

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


    public static boolean updateProduct(Product product, InputStream imageStream) {
        String sqlWithImage = "UPDATE products SET item_id = ?, item_name = ?, description = ?, price = ?, quantity = ?, image = ? WHERE id = ?";
        String sqlWithoutImage = "UPDATE products SET item_id = ?, item_name = ?, description = ?, price = ?, quantity = ? WHERE id = ?";

        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement pst;
            if (imageStream != null) {
                pst = conn.prepareStatement(sqlWithImage);
                pst.setString(1, product.getItemId());
                pst.setString(2, product.getName());
                pst.setString(3, product.getDescription());
                pst.setDouble(4, product.getPrice());
                pst.setInt(5, product.getQuantity());
                pst.setBlob(6, imageStream);
                pst.setInt(7, product.getId());
            } else {
                pst = conn.prepareStatement(sqlWithoutImage);
                pst.setString(1, product.getItemId());
                pst.setString(2, product.getName());
                pst.setString(3, product.getDescription());
                pst.setDouble(4, product.getPrice());
                pst.setInt(5, product.getQuantity());
                pst.setInt(6, product.getId());
            }

            int rows = pst.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



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
}

