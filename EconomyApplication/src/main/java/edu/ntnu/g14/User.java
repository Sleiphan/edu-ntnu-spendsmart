package edu.ntnu.g14;

import java.util.ArrayList;
import java.util.List;

public class User extends Personalia {
    private final List<Account> accounts;
    private final Invoice[] invoices;
    private final List<Transaction> transactions;
    private final Budget budget;

    public User(Account[] accounts, Invoice[] invoices,
        Login loginInfo, String email, String lastName, String firstName,
        Transaction[] transactions, Budget budget) {
        super(loginInfo, email, lastName, firstName);
        this.transactions = new ArrayList<>();

        this.invoices = invoices;
        this.budget = budget;
        this.accounts = new ArrayList<>();
        if (transactions != null) {
            this.transactions.addAll(List.of(transactions));
        }
        if (accounts != null) {
            this.accounts.addAll(List.of(accounts));
        }
    }

    public Account[] getAccounts() {
        return this.accounts.toArray(Account[]::new);
    }
    public List<Account> getAccountsAsList() {
        return this.accounts;
    }
    public void addAccount(Account account) {
        this.accounts.add(account);
    }
    public Invoice[] getAllInvoices() {
        return invoices;
    }
    public Transaction[] getTransactions(){
        return this.transactions.toArray(Transaction[]::new);
    }
    public List<Transaction> getTransactionsAsList(){
        return this.transactions;
    }

    public Budget getBudget(){
        return budget;
    }

}
