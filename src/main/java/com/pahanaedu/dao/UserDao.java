package com.pahanaedu.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import com.pahanaedu.model.User;
import com.pahanaedu.util.DBUtil;

public class UserDao {

    /**
     * Validate user credentials during login
     */
	public static User validateUser(String email, String password) {
	    User user = null;
	    String sql = "SELECT * FROM users WHERE uemail = ?";

	    try (Connection con = DBUtil.getConnection();
	         PreparedStatement pst = con.prepareStatement(sql)) {

	        pst.setString(1, email);

	        try (ResultSet rs = pst.executeQuery()) {
	            if (rs.next()) {
	                String hashedPwd = rs.getString("upwd");

	                boolean match = false;

	                if (hashedPwd != null) {
	                    if (hashedPwd.startsWith("$2a$") || hashedPwd.startsWith("$2b$")) {
	                        // It's a bcrypt hash
	                        match = BCrypt.checkpw(password, hashedPwd);  // ✅ Good
	                    } else {
	                        // Plain text fallback (for old users)
	                        match = password.equals(hashedPwd);          // 🔁 Fallback
	                    }
	                }

	                if (match) {
	                    user = new User();
	                    user.setName(rs.getString("uname"));
	                    user.setEmail(rs.getString("uemail"));
	                    user.setPassword(hashedPwd);
	                    user.setMobile(rs.getString("umobile"));
	                    user.setRole(rs.getString("urole"));
	                }
	            }
	        }

	    } catch (Exception e) {
	        System.out.println("Error in validateUser:");
	        e.printStackTrace();
	    }

	    return user;
	}

    /**
     * Check if the email already exists (for forgot password, registration)
     */
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
            System.out.println("Error in isEmailExists:");
            e.printStackTrace();
        }
        return exists;
    }

 // Add this method:
    public static boolean isUempidExists(String uempid) {
        boolean exists = false;
        String sql = "SELECT uempid FROM users WHERE uempid = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, uempid);
            try (ResultSet rs = pst.executeQuery()) {
                exists = rs.next();
            }

        } catch (Exception e) {
            System.out.println("Error in isUempidExists:");
            e.printStackTrace();
        }
        return exists;
    }

    /**
     * Register a new user
     */
 // Modify this method:
    public static boolean addUser(User user) {
        boolean isSuccess = false;
        String sql = "INSERT INTO users (uempid, uname, uemail, upwd, umobile, urole, uphoto) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, user.getUempid());
            pst.setString(2, user.getName());
            pst.setString(3, user.getEmail());
            pst.setString(4, user.getPassword());
            pst.setString(5, user.getMobile());
            pst.setString(6, user.getRole());

            if (user.getPhoto() != null) {
                pst.setBlob(7, user.getPhoto());
            } else {
                pst.setNull(7, java.sql.Types.BLOB);
            }

            int rowsInserted = pst.executeUpdate();
            isSuccess = rowsInserted > 0;

        } catch (Exception e) {
            System.out.println("Error in addUser:");
            e.printStackTrace();
        }

        return isSuccess;
    }

    /**
     * Get all users from database
     */
    public static List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

        	while (rs.next()) {
        	    User user = new User();
        	    user.setUempid(rs.getString("uempid")); // ✅ Added
        	    user.setName(rs.getString("uname"));
        	    user.setEmail(rs.getString("uemail"));
        	    user.setPassword(rs.getString("upwd"));
        	    user.setMobile(rs.getString("umobile"));
        	    user.setRole(rs.getString("urole"));
        	    userList.add(user);
        	}

        } catch (Exception e) {
            System.out.println("Error in getAllUsers:");
            e.printStackTrace();
        }

        return userList;
    }

    /**
     * Count total users in DB
     */
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
            System.out.println("Error in getUserCount:");
            e.printStackTrace();
        }

        return count;
    }

    /**
     * Delete a user using email (admin feature maybe)
     */
    public static boolean deleteUserByUempid(String uempid) {
        String sql = "DELETE FROM users WHERE uempid = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, uempid);
            return pst.executeUpdate() > 0;

        } catch (Exception e) {
            System.out.println("Error in deleteUserByUempid:");
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Update user's password (used in reset password flow)
     */
    public static boolean updatePassword(String email, String hashedPassword) {
        boolean rowUpdated = false;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE users SET upwd = ? WHERE uemail = ?")) {
            stmt.setString(1, hashedPassword);  // Already hashed by servlet
            stmt.setString(2, email);
            int rows = stmt.executeUpdate();
            System.out.println("Update Password affected rows: " + rows);
            rowUpdated = rows > 0;
        } catch (Exception e) {
            System.out.println("Error in updatePassword:");
            e.printStackTrace();
        }
        return rowUpdated;
    }


    // Check if email is registered
    public static boolean isEmailRegistered(String email) {
        boolean exists = false;
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT * FROM users WHERE uemail = ?";  // FIXED COLUMN NAME
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            exists = rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exists;
    }
    
    public static boolean updateUser(String originalEmail, User updatedUser, InputStream photoStream) {
        boolean isUpdated = false;

        String sqlWithPhoto = "UPDATE users SET uempid = ?, uname = ?, uemail = ?, umobile = ?, urole = ?, uphoto = ? WHERE uemail = ?";
        String sqlWithoutPhoto = "UPDATE users SET uempid = ?, uname = ?, uemail = ?, umobile = ?, urole = ? WHERE uemail = ?";

        try (Connection con = DBUtil.getConnection()) {
            PreparedStatement pst;

            if (photoStream != null) {
                pst = con.prepareStatement(sqlWithPhoto);
                pst.setString(1, updatedUser.getUempid());
                pst.setString(2, updatedUser.getName());
                pst.setString(3, updatedUser.getEmail());
                pst.setString(4, updatedUser.getMobile());
                pst.setString(5, updatedUser.getRole());
                pst.setBlob(6, photoStream);
                pst.setString(7, originalEmail);
            } else {
                pst = con.prepareStatement(sqlWithoutPhoto);
                pst.setString(1, updatedUser.getUempid());
                pst.setString(2, updatedUser.getName());
                pst.setString(3, updatedUser.getEmail());
                pst.setString(4, updatedUser.getMobile());
                pst.setString(5, updatedUser.getRole());
                pst.setString(6, originalEmail);
            }

            int rows = pst.executeUpdate();
            isUpdated = rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isUpdated;
    }


}






