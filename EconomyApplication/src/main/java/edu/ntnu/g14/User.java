package edu.ntnu.g14;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

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

    public String amountAllAccounts() {

        return this.accounts
                .stream()
                .map(Account::getAmount)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add)
                .toString();
    }

    public String expensesLast30Days() {
        return incomeOrExpensesAllAccountsLast30Days(false);
    }
    public String incomeLast30Days() {
        return incomeOrExpensesAllAccountsLast30Days(true);
    }
    private String incomeOrExpensesAllAccountsLast30Days(Boolean incomeOrExpenses) {
        Supplier<Stream<String>> accountNumbers = () -> accounts
                .stream()
                .filter(account -> !account.getAccountType().equals(AccountCategory.PENSION_ACCOUNT))
                .map(Account::getAccountNumber);

        return this.transactions
                .stream()
                .filter(transaction ->
                        incomeOrExpenses ? accountNumbers.get()
                                .anyMatch(accountNumber -> accountNumber.equals(transaction.getToAccountNumber()))
                                : accountNumbers.get().anyMatch(accountNumber -> accountNumber.equals(transaction.getFromAccountNumber())))
                .filter(transaction ->
                        transaction.getDateOfTransaction().isAfter(LocalDate.now().minusDays(30))
                                && transaction.getDateOfTransaction().isBefore(LocalDate.now().plusDays(1)))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add).toString();
    }

}
