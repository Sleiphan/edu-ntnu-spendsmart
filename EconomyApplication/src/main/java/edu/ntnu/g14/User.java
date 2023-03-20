package edu.ntnu.g14;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class User extends Personalia {
    private HashMap<String, Account> accounts;
    private HashMap<String, Invoice> invoices;
    private Transaction[] transactions;
    private Budget budget;

    public User(HashMap<String, Account> accounts,HashMap<String, Invoice> invoices,
        Login loginInfo, String email, String lastName, String firstName,
        Transaction[] transactions, Budget budget) {
        super(loginInfo,email,lastName,firstName);
        this.transactions=transactions;
        this.accounts=accounts;
        this.invoices=invoices;
        this.budget = budget;
    }

    public HashMap<String, Account> getAllAccounts() {
        return accounts;
    }
    public HashMap<String, Invoice> getAllInvoices() {
        return invoices;
    }

    public Transaction[] getTransactions(){
        return transactions;
    }

    public Budget getBudget(){
        return budget;
    }
}
