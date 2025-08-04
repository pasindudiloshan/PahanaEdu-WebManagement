package com.pahanaedu.test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import com.pahanaedu.util.DBUtil;

class DBUtilTest {

    @Test
    void testGetConnection() {
        try (Connection conn = DBUtil.getConnection()) {
            assertNotNull(conn, "Connection should not be null");
            assertFalse(conn.isClosed(), "Connection should be open");
            System.out.println("✅ Database connection test passed.");
        } catch (SQLException e) {
            fail("Database connection failed: " + e.getMessage());
        }
    }
}
