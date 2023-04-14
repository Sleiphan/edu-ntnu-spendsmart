package edu.ntnu.g14;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class User extends Personalia {
    private final List<Account> accounts;
    private final List<Invoice> invoices;
    private final List<Transaction> transactions;
    private Budget budget;

    public User(Account[] accounts, Invoice[] invoices,
        Login loginInfo, String email, String lastName, String firstName,
        Transaction[] transactions, Budget budget) {
        super(loginInfo, email, lastName, firstName);
        this.transactions = new ArrayList<>();

        this.invoices = invoices != null ? Arrays.asList(invoices) : new ArrayList<>();
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
    public List<Invoice> getAllInvoices() {
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

    public void setBudget(Budget newBudget) {
        this.budget = newBudget;
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
    public boolean checkIfAccountNameIsOccupied(String accountName) {
        return this.accounts.stream()
                .filter(account -> account.getAccountName()
                        .equals(accountName))
                .findAny().isEmpty();
    }
    public Account getAccountWithAccountName(String accountName) {
        return this.accounts.stream()
                .filter(account -> account.getAccountName()
                        .equals(accountName))
                .findFirst()
                .orElseThrow();
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

    public double getAmountSpentOnCategoryLast30Days(String category) {
        return getTransactionsAsList().stream()
                .filter(transaction -> getAccountsAsList().stream()
                        .map(Account::getAccountNumber)
                        .anyMatch(accountNumber -> accountNumber.equals(transaction.getFromAccountNumber())))
                .filter(transaction ->
                        transaction.getDateOfTransaction().isAfter(LocalDate.now().minusDays(30))
                                && transaction.getDateOfTransaction().isBefore(LocalDate.now().plusDays(1)))
                .filter(transaction -> transaction.getCategory()
                .equals(BudgetCategory.valueOf(category
                        .replaceAll(" ", "_")
                        .toUpperCase())))
                .map(Transaction::getAmount).reduce(new BigDecimal(0), BigDecimal::add).doubleValue();
    }

    public double getAmountSpentOnCategoryLastYear(String category) {
        LocalDate startOfLastYear = LocalDate.ofYearDay(LocalDate.now().getYear() - 1, 1);
        LocalDate endOfLastYear = LocalDate.ofYearDay(LocalDate.now().getYear() - 1, 365);

        if (startOfLastYear.isLeapYear()) {
            endOfLastYear = LocalDate.ofYearDay(LocalDate.now().getYear() - 1, 366);
        }
        LocalDate finalEndOfLastYear = endOfLastYear;

        return getTransactionsAsList().stream()
                .filter(transaction -> getAccountsAsList().stream()
                        .map(Account::getAccountNumber)
                        .anyMatch(accountNumber -> accountNumber.equals(transaction.getFromAccountNumber())))
                .filter(transaction ->
                        transaction.getDateOfTransaction().isAfter(startOfLastYear)
                                && transaction.getDateOfTransaction().isBefore(finalEndOfLastYear))
                .filter(transaction -> transaction.getCategory()
                        .equals(BudgetCategory.valueOf(category
                                .replaceAll(" ", "_")
                                .toUpperCase())))
                .map(Transaction::getAmount).reduce(new BigDecimal(0), BigDecimal::add).doubleValue();
    }
}
