package com.pahanaedu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.pahanaedu.model.User;
import com.pahanaedu.util.DBUtil;

public class UserDao {

    // Validate user login
    public static User validateUser(String email, String password) {
        User user = null;
        String sql = "SELECT * FROM users WHERE uemail = ? AND upwd = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, email);
            pst.setString(2, password);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setName(rs.getString("uname"));
                    user.setEmail(rs.getString("uemail"));
                    user.setPassword(rs.getString("upwd"));
                    user.setMobile(rs.getString("umobile"));
                    user.setRole(rs.getString("urole"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    // Check if email already exists
    public static boolean isEmailExists(String email) {
        boolean exists = false;
        String sql = "SELECT uemail FROM users WHERE uemail = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {
                exists = rs.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return exists;
    }

    // Add new user
    public static boolean addUser(User user) {
        boolean isSuccess = false;
        String sql = "INSERT INTO users (uname, uemail, upwd, umobile, urole, uphoto) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, user.getName());
            pst.setString(2, user.getEmail());
            pst.setString(3, user.getPassword());
            pst.setString(4, user.getMobile());
            pst.setString(5, user.getRole());

            if (user.getPhoto() != null) {
                pst.setBlob(6, user.getPhoto());
            } else {
                pst.setNull(6, java.sql.Types.BLOB);
            }

            int rowsInserted = pst.executeUpdate();
            isSuccess = rowsInserted > 0;

        } catch (Exception e) {
            System.out.println("Error in addUser DAO:");
            e.printStackTrace();
        }

        return isSuccess;
    }

    // Get all users
    public static List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setName(rs.getString("uname"));
                user.setEmail(rs.getString("uemail"));
                user.setPassword(rs.getString("upwd"));
                user.setMobile(rs.getString("umobile"));
                user.setRole(rs.getString("urole"));
                userList.add(user);
            }

        } catch (Exception e) {
            System.out.println("Error in getAllUsers DAO:");
            e.printStackTrace();
        }

        return userList;
    }

    // Get total user count
    public static int getUserCount() {
        int count = 0;
        String sql = "SELECT COUNT(*) AS total FROM users";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt("total");
            }

        } catch (Exception e) {
            System.out.println("Error in getUserCount DAO:");
            e.printStackTrace();
        }

        return count;
    }

    // Delete user by email
    public static boolean deleteUserByEmail(String email) {
        String sql = "DELETE FROM users WHERE uemail = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, email);
            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error in deleteUserByEmail DAO:");
            e.printStackTrace();
            return false;
        }
    }
}


