package edu.ntnu.g14;

import edu.ntnu.g14.frontend.ApplicationObjects;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class User extends Personalia {
    private final List<Account> accounts;
    private final List<Invoice> invoices;
    private final List<Transaction> transactions;
    private Budget budget;
    private static final String CSV_DELIMITER = ",";

    public User(Account[] accounts, Invoice[] invoices,
        Login loginInfo, String email, String lastName, String firstName,
        Transaction[] transactions, Budget budget) {
        super(loginInfo, email, lastName, firstName);
        this.transactions = new ArrayList<>();

        this.invoices = invoices != null ? Arrays.stream(invoices).collect(Collectors.toList()) : new ArrayList<>();
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

    public String toCSVString() {
        return this.getLoginInfo().getUserId() +
                CSV_DELIMITER + this.getLoginInfo().getUserName() +
                CSV_DELIMITER + this.getEmail() +
                CSV_DELIMITER + this.getLoginInfo().getPassword() +
                CSV_DELIMITER + this.getFirstName() +
                CSV_DELIMITER + this.getLastName();
    }
    public void setBudget(Budget newBudget) {
        this.budget = newBudget;
    }

    public String amountAllAccounts() {

        return ApplicationObjects.numberRegex(this.accounts
                .stream()
                .filter(account -> !account.getAccountType().equals(AccountCategory.PENSION_ACCOUNT))
                .map(Account::getAmount)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add)
                .toString());
    }

    public String expensesLastYear() {
        return incomeOrExpensesLastYear(false);
    }
    public String incomeLastYear() {
        return incomeOrExpensesLastYear(true);
    }
    public String expensesLast30Days() {
        return incomeOrExpensesAllAccountsLast30Days(false);
    }
    public String incomeLast30Days() {
        return incomeOrExpensesAllAccountsLast30Days(true);
    }
    public boolean checkIfAccountNameIsOccupied(String accountName) {
        return this.accounts.stream()
                .anyMatch(account -> account.getAccountName()
                        .equalsIgnoreCase(accountName));
    }
    public boolean checkIfAccountNumberIsOccupied(String accountNumber) {
        return  !this.accounts.stream()
                .anyMatch(account -> account.getAccountNumber()
                        .equalsIgnoreCase(accountNumber));
    }
    public Account getAccountWithAccountName(String accountName) {
        return this.accounts.stream()
                .filter(account -> account.getAccountName()
                        .equals(accountName))
                .findFirst()
                .orElseThrow();
    }
    public Account getAccountWithAccountNumber(String accountNumber) {
        return this.accounts.stream()
                .filter(account -> account.getAccountNumber()
                        .equals(accountNumber))
                .findFirst()
                .orElseThrow();
    }
    public void removeAccount(Account account){
        this.accounts.remove(account);
    }
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }
    private String incomeOrExpensesAllAccountsLast30Days(boolean incomeOrExpenses) {
        Supplier<Stream<String>> accountNumbers = () -> accounts
                .stream()
                .map(Account::getAccountNumber);

        return ApplicationObjects.numberRegex(this.transactions
                .stream()
                .filter(transaction ->
                        incomeOrExpenses ? accountNumbers.get()
                                .anyMatch(accountNumber -> accountNumber.equals(transaction.getToAccountNumber()))
                                : accountNumbers.get().anyMatch(accountNumber -> accountNumber.equals(transaction.getFromAccountNumber())))
                .filter(transaction ->
                        transaction.getDateOfTransaction().isAfter(LocalDate.now().minusDays(30))
                                && transaction.getDateOfTransaction().isBefore(LocalDate.now().plusDays(1)))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add).toString());
    }
    private String incomeOrExpensesLastYear(boolean incomeOrExpenses) {
        LocalDate startOfLastYear = LocalDate.ofYearDay(LocalDate.now().getYear() - 1, 1);
        LocalDate endOfLastYear = LocalDate.ofYearDay(LocalDate.now().getYear() - 1, 365);
        Supplier<Stream<String>> accountNumbers = () -> accounts
                .stream()
                .filter(account -> !account.getAccountType().equals(AccountCategory.PENSION_ACCOUNT))
                .map(Account::getAccountNumber);

        return ApplicationObjects.numberRegex(this.transactions
                .stream()
                .filter(transaction ->
                        incomeOrExpenses ? accountNumbers.get()
                                .anyMatch(accountNumber -> accountNumber.equals(transaction.getToAccountNumber()))
                                : accountNumbers.get().anyMatch(accountNumber -> accountNumber.equals(transaction.getFromAccountNumber())))
                .filter(transaction ->
                        transaction.getDateOfTransaction().isAfter(startOfLastYear)
                                && transaction.getDateOfTransaction().isBefore(endOfLastYear))
                .map(Transaction::getAmount)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add).toString());

    }
    public double getTotalExpenseOfCategoryLast30Days(String category) {
        return getTotalExpenseOrIncomeOfCategoryLast30Days(category);
    }

    public double getTotalIncomeOfCategoryLast30Days(String category) {
        return getTotalExpenseOrIncomeOfCategoryLast30Days(category);
    }
    private double getTotalExpenseOrIncomeOfCategoryLast30Days(String category) {
        return getTransactionsAsList().stream()
                .filter(transaction ->
                        transaction.getDateOfTransaction().isAfter(LocalDate.now().minusDays(30))
                                && transaction.getDateOfTransaction().isBefore(LocalDate.now().plusDays(1)))
                .filter(transaction -> transaction.getCategory()
                .equals(BudgetCategory.valueOf(category
                        .replaceAll(" ", "_")
                        .toUpperCase())))
                .map(Transaction::getAmount).reduce(new BigDecimal(0), BigDecimal::add).doubleValue();
    }

    public double getTotalExpenseOfCategoryLastYear(String category) {
        return getTotalExpenseOrIncomeOfCategoryLastYear(category, true);
    }
    public double getTotalIncomeOfCategoryLastYear(String category) {
        return getTotalExpenseOrIncomeOfCategoryLastYear(category, false);
    }
    private double getTotalExpenseOrIncomeOfCategoryLastYear(String category, boolean expenseOrIncome) {
        LocalDate startOfLastYear = LocalDate.ofYearDay(LocalDate.now().getYear() - 1, 1);
        LocalDate endOfLastYear = LocalDate.ofYearDay(LocalDate.now().getYear() - 1, 365);

        if (startOfLastYear.isLeapYear()) {
            endOfLastYear = LocalDate.ofYearDay(LocalDate.now().getYear() - 1, 366);
        }
        LocalDate finalEndOfLastYear = endOfLastYear;

        return getTransactionsAsList().stream()
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
