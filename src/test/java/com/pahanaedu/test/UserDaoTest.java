package com.pahanaedu.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;

import com.pahanaedu.dao.UserDao;
import com.pahanaedu.model.User;

public class UserDaoTest {

    private static final String TEST_EMAIL = "testuser@example.com";
    private static final String TEST_PASSWORD = "123456";
    private static final String TEST_UEMPID = "PEB-EMP-001";

    @BeforeAll
    public static void setup() {
        User user = new User();
        user.setUempid(TEST_UEMPID);
        user.setName("Test User");
        user.setEmail(TEST_EMAIL);

        // Hash the password before saving
        String hashed = BCrypt.hashpw(TEST_PASSWORD, BCrypt.gensalt());
        user.setPassword(hashed);

        user.setMobile("1234567890");
        user.setRole("Admin");
        user.setPhoto(new ByteArrayInputStream(new byte[]{1, 2, 3}));

        UserDao.addUser(user);
    }

    @AfterAll
    public static void cleanup() {
        // Clean up test user
        UserDao.deleteUserByUempid(TEST_UEMPID);
    }

    @Test
    public void testValidateUser() {
        User user = UserDao.validateUser(TEST_EMAIL, TEST_PASSWORD);
        assertNotNull(user, "User should be found with correct credentials");
        assertEquals(TEST_EMAIL, user.getEmail(), "Emails should match");
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = UserDao.getAllUsers();
        assertNotNull(users, "User list should not be null");
        assertTrue(users.size() > 0, "There should be at least one user");
    }

    @Test
    public void testGetUserCount() {
        int count = UserDao.getUserCount();
        assertTrue(count >= 0, "User count should be non-negative");
    }

    @Test
    public void testDeleteUserByUempid() {
        // Add temporary user with a unique ID to delete
        User tempUser = new User();
        tempUser.setUempid("PEB-EMP-002");
        tempUser.setName("Temp User");
        tempUser.setEmail("temp@example.com");

        // Hash password here as well for consistency
        String hashed = BCrypt.hashpw("123456", BCrypt.gensalt());
        tempUser.setPassword(hashed);

        tempUser.setMobile("0000000000");
        tempUser.setRole("Admin");

        UserDao.addUser(tempUser);

        boolean deleted = UserDao.deleteUserByUempid("PEB-EMP-002");
        assertTrue(deleted, "Temporary user should be deleted");
    }
}



