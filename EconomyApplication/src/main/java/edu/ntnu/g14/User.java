package edu.ntnu.g14;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class User extends Personalia {
    private HashMap<String, Account> accounts;
    private HashMap<String, Invoice> invoices;
    private String userId;

    public User(HashMap<String, Account> accounts,HashMap<String, Invoice> invoices, String userId ) {
        this.accounts=accounts;
        this.invoices=invoices;
        this.userId=userId;
        // personalia object?
    }

    public List<Account> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }
    public List<Invoice> getAllInvoices() {
        return new ArrayList<>(invoices.values());
    }

    public String getUserId() {
        return userId;
    }
}
