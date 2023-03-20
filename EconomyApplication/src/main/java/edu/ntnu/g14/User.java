package edu.ntnu.g14;

public class User extends Personalia {
    private Account[] accounts;
    private Invoice[] invoices;
    private Transaction[] transactions;
    private Budget budget;

    public User(Account[] accounts, Invoice[] invoices,
        Login loginInfo, String email, String lastName, String firstName,
        Transaction[] transactions, Budget budget) {
        super(loginInfo,email,lastName,firstName);
        this.transactions=transactions;
        this.accounts=accounts;
        this.invoices=invoices;
        this.budget = budget;
    }

    public Account[] getAllAccounts() {
        return accounts;
    }
    public Invoice[] getAllInvoices() {
        return invoices;
    }

    public Transaction[] getTransactions(){
        return transactions;
    }

    public Budget getBudget(){
        return budget;
    }
}
