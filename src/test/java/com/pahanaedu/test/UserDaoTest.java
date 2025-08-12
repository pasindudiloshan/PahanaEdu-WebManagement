package com.pahanaedu.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;

import com.pahanaedu.dao.UserDao;
import com.pahanaedu.model.User;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDaoTest {

    private static final String TEST_EMAIL = "testuser@example.com";
    private static final String TEST_PASSWORD = "123456";
    private static final String TEST_UEMPID = "PEB-EMP-TEST-001";

    @BeforeAll
    public void setup() {
        User user = new User();
        user.setUempid(TEST_UEMPID);
        user.setName("Test User");
        user.setEmail(TEST_EMAIL);

        String hashed = BCrypt.hashpw(TEST_PASSWORD, BCrypt.gensalt());
        user.setPassword(hashed);

        user.setMobile("1234567890");
        user.setRole("Admin");
        user.setPhoto(new ByteArrayInputStream(new byte[]{1, 2, 3}));

        boolean added = UserDao.addUser(user);
        assertTrue(added, "Test user should be added successfully");
    }

    @AfterAll
    public void cleanup() {
        UserDao.deleteUserByUempid(TEST_UEMPID);
    }

    @Test
    public void testValidateUser() {
        User user = UserDao.validateUser(TEST_EMAIL, TEST_PASSWORD);
        assertNotNull(user, "User should be found with correct credentials");
        assertEquals(TEST_EMAIL, user.getEmail(), "Emails should match");
    }

    @Test
    public void testValidateUserWithWrongPassword() {
        User user = UserDao.validateUser(TEST_EMAIL, "wrongpassword");
        assertNull(user, "User should be null with wrong password");
    }

    @Test
    public void testIsEmailExists() {
        assertTrue(UserDao.isEmailExists(TEST_EMAIL), "Existing email should return true");
        assertFalse(UserDao.isEmailExists("nonexistent@example.com"), "Non-existing email should return false");
    }

    @Test
    public void testIsUempidExists() {
        assertTrue(UserDao.isUempidExists(TEST_UEMPID), "Existing uempid should return true");
        assertFalse(UserDao.isUempidExists("NON-EXIST-EMPID"), "Non-existing uempid should return false");
    }

    @Test
    public void testAddUserWithInvalidRole() {
        User invalidUser = new User();
        invalidUser.setUempid("PEB-EMP-INVALID-001");
        invalidUser.setName("Invalid Role User");
        invalidUser.setEmail("invalidrole@example.com");
        invalidUser.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        invalidUser.setMobile("0000000000");
        invalidUser.setRole("InvalidRole"); // not allowed
        invalidUser.setPhoto(null);

        boolean added = UserDao.addUser(invalidUser);
        assertFalse(added, "User with invalid role should not be added");
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
    public void testGetUserCountByRole() {
        int adminCount = UserDao.getUserCountByRole("Admin");
        assertTrue(adminCount >= 0, "Admin user count should be non-negative");

        int invalidRoleCount = UserDao.getUserCountByRole("InvalidRole");
        assertEquals(0, invalidRoleCount, "Invalid role user count should be zero");
    }

    @Test
    public void testDeleteUserByUempid() {
        String tempUempid = "PEB-EMP-TEMP-001";
        User tempUser = new User();
        tempUser.setUempid(tempUempid);
        tempUser.setName("Temp User");
        tempUser.setEmail("tempuser@example.com");
        tempUser.setPassword(BCrypt.hashpw("123456", BCrypt.gensalt()));
        tempUser.setMobile("0000000000");
        tempUser.setRole("Admin");
        tempUser.setPhoto(new ByteArrayInputStream(new byte[]{1, 2, 3}));

        boolean added = UserDao.addUser(tempUser);
        assertTrue(added, "Temporary user should be added");

        boolean deleted = UserDao.deleteUserByUempid(tempUempid);
        assertTrue(deleted, "Temporary user should be deleted");
    }

    @Test
    public void testUpdatePassword() {
        String newHashed = BCrypt.hashpw("newpassword", BCrypt.gensalt());
        boolean updated = UserDao.updatePassword(TEST_EMAIL, newHashed);
        assertTrue(updated, "Password should be updated");

        // Check login with new password works
        User user = UserDao.validateUser(TEST_EMAIL, "newpassword");
        assertNotNull(user, "User should authenticate with new password");

        // Reset password to original for other tests
        UserDao.updatePassword(TEST_EMAIL, BCrypt.hashpw(TEST_PASSWORD, BCrypt.gensalt()));
    }

    @Test
    public void testIsEmailRegistered() {
        assertTrue(UserDao.isEmailRegistered(TEST_EMAIL), "Email should be registered");
        assertFalse(UserDao.isEmailRegistered("notregistered@example.com"), "Email should not be registered");
    }

    @Test
    public void testUpdateUserWithValidData() throws Exception {
        // Prepare updated user info
        User updatedUser = new User();
        updatedUser.setUempid(TEST_UEMPID);
        updatedUser.setName("Updated Name");
        updatedUser.setEmail(TEST_EMAIL);
        updatedUser.setMobile("9999999999");
        updatedUser.setRole("Admin");

        // InputStream for photo (can be null to test no-photo path)
        InputStream photoStream = new ByteArrayInputStream(new byte[]{4, 5, 6});

        boolean updated = UserDao.updateUser(TEST_EMAIL, updatedUser, photoStream);
        assertTrue(updated, "User should be updated with photo");

        // Update without photo (null InputStream)
        updatedUser.setName("Updated Name No Photo");
        updated = UserDao.updateUser(TEST_EMAIL, updatedUser, null);
        assertTrue(updated, "User should be updated without photo");
    }

    @Test
    public void testUpdateUserWithInvalidRole() {
        User updatedUser = new User();
        updatedUser.setUempid(TEST_UEMPID);
        updatedUser.setName("Invalid Role Update");
        updatedUser.setEmail(TEST_EMAIL);
        updatedUser.setMobile("9999999999");
        updatedUser.setRole("InvalidRole");  // invalid role

        boolean updated = UserDao.updateUser(TEST_EMAIL, updatedUser, null);
        assertFalse(updated, "Update should fail due to invalid role");
    }

    @Test
    public void testUpdateUserWithDuplicateEmail() {
        // Assume there's already another user with this email
        String duplicateEmail = "duplicate@example.com";

        User user = new User();
        user.setUempid("PEB-EMP-DUP-001");
        user.setName("Duplicate User");
        user.setEmail(duplicateEmail);
        user.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        user.setMobile("1111111111");
        user.setRole("Admin");
        user.setPhoto(null);

        UserDao.addUser(user);

        User updatedUser = new User();
        updatedUser.setUempid(TEST_UEMPID);
        updatedUser.setName("Attempt Duplicate Email");
        updatedUser.setEmail(duplicateEmail); // Trying to update to existing email
        updatedUser.setMobile("9999999999");
        updatedUser.setRole("Admin");

        boolean updated = UserDao.updateUser(TEST_EMAIL, updatedUser, null);
        assertFalse(updated, "Update should fail due to duplicate email");

        // Cleanup
        UserDao.deleteUserByUempid("PEB-EMP-DUP-001");
    }

    @Test
    public void testUpdateUserWithDuplicateUempid() {
        // Create a second user with a unique email but a specific uempid
        String secondEmail = "seconduser@example.com";
        String duplicateUempid = "PEB-EMP-DUPLICATE-UEMPID";

        User secondUser = new User();
        secondUser.setUempid(duplicateUempid);
        secondUser.setName("Second User");
        secondUser.setEmail(secondEmail);
        secondUser.setPassword(BCrypt.hashpw("password", BCrypt.gensalt()));
        secondUser.setMobile("2222222222");
        secondUser.setRole("Admin");
        secondUser.setPhoto(null);

        UserDao.addUser(secondUser);

        User updatedUser = new User();
        updatedUser.setUempid(duplicateUempid); // Trying to update to duplicate uempid
        updatedUser.setName("Attempt Duplicate Uempid");
        updatedUser.setEmail(TEST_EMAIL);
        updatedUser.setMobile("9999999999");
        updatedUser.setRole("Admin");

        boolean updated = UserDao.updateUser(TEST_EMAIL, updatedUser, null);
        assertFalse(updated, "Update should fail due to duplicate uempid");

        // Cleanup
        UserDao.deleteUserByUempid(duplicateUempid);
    }
}
