package edu.ntnu.g14;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserManagementTest {

    private UserManagement userManagement;

    @BeforeEach
    public void setUp() {
        userManagement = new UserManagement();
    }

    @Test
    public void testRemoveUser() {
        // Create a sample user
        Login loginInfo = new Login("testuser", "password", "testuser123");
        // Add the user to the UserManagement
        userManagement.createNewUser("testuser123", new Account[]{}, new Invoice[]{}, loginInfo, "email@example.com", "John", "Doe", new Transaction[]{}, new Budget((byte) 12, GenderCategory.MALE));

        // Verify that the user was added
        assertTrue(userManagement.verifyUser("testuser123"));

        // Remove the user
        userManagement.removeUser("testuser123");

        // Verify that the user was removed
        assertFalse(userManagement.verifyUser("testuser123"));
    }

    @Test
    public void testVerifyUser() {
        // Create a sample user
        Login loginInfo = new Login("testuser", "password", "testuser123");

        // Add the user to the UserManagement
        userManagement.createNewUser("testuser123", new Account[]{}, new Invoice[]{}, loginInfo, "email@example.com", "John", "Doe", new Transaction[]{}, new Budget((byte) 12, GenderCategory.MALE));

        assertTrue(userManagement.verifyUser("testuser123"));
        assertFalse(userManagement.verifyUser("notAUser"));
    }
}
