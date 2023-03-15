package edu.ntnu.g14;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class User extends Personalia {
    private HashMap<String, Account> accounts;
    private HashMap<String, Invoice> invoices;

    public User(HashMap<String, Account> accounts,HashMap<String, Invoice> invoices,
        Login loginInfo, String email, String lastName, String firstName,
        Transaction[] transactions, Budget budget) {
        super(loginInfo,email,lastName,firstName);
        this.accounts=accounts;
        this.invoices=invoices;
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
