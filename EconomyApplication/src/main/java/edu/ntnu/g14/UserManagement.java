package edu.ntnu.g14;
import java.util.HashMap;

public class UserManagement {
    private HashMap<String, User> users;

    public UserManagement() {
        this.users = new HashMap<>();
    }

    public void createNewUser(String userId, HashMap<String, Account> accounts, HashMap<String, Invoice> invoices, Login loginInfo, String email, String lastName, String firstName) {
        User user = new User(accounts, invoices, userId, loginInfo, email, lastName, firstName);
        users.put(userId, user);
    }

    public void removeUser(String userId) {
        users.remove(userId);
    }

    public boolean verifyUser(String userId) {
        return users.containsKey(userId);
    }
}
