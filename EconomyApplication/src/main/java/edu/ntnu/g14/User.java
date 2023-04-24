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

/**
 * The User class represents a user in the banking system.
 * It inherits from the Personalia class.
 */
public class User extends Personalia {
    private final List<Account> accounts;
    private final List<Invoice> invoices;
    private final List<Transaction> transactions;
    private Budget budget;
    private static final String CSV_DELIMITER = ",";

    /**
    * Constructs a new User object with the given information.
    *
    * @param accounts     an array of accounts held by the user
    * @param invoices     an array of invoices issued to the user
    * @param loginInfo    the login information of the user
    * @param email        the email of the user
    * @param lastName     the last name of the user
    * @param firstName    the first name of the user
    * @param transactions an array of transactions associated with the user
    * @param budget       the budget of the user
    */
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

    /**
    * Gets the accounts held by the user as an array.
    *
    * @return an array of accounts held by the user
    */
    public Account[] getAccounts() {
        return this.accounts.toArray(Account[]::new);
    }

    /**
    * Gets the accounts held by the user as a list.
    *
    * @return a list of accounts held by the user
    */
    public List<Account> getAccountsAsList() {
        return this.accounts;
    }

    /**
    * Adds the given account to the accounts held by the user.
    *
    * @param account the account to be added
    */
    public void addAccount(Account account) {
        this.accounts.add(account);
    }

    /**
    * Gets all the invoices issued to the user.
    *
    * @return a list of all the invoices issued to the user
    */
    public List<Invoice> getAllInvoices() {
        return invoices;
    }

    /**
    * Gets the transactions associated with the user as an array.
    *
    * @return an array of transactions associated with the user
    */
    public Transaction[] getTransactions(){
        return this.transactions.toArray(Transaction[]::new);
    }

    /**
    * Gets the transactions associated with the user as a list.
    *
    * @return a list of transactions associated with the user
    */
    public List<Transaction> getTransactionsAsList(){
        return this.transactions;
    }

    /**
    * Returns the budget of the user.
    * 
    * @return the budget of the user
    */
    public Budget getBudget(){
        return budget;
    }

    /**
    * Returns the user information as a comma-separated values string.
    * 
    * @return the user information as a CSV string
    */
    public String toCSVString() {
        return this.getLoginInfo().getUserId() +
                CSV_DELIMITER + this.getLoginInfo().getUserName() +
                CSV_DELIMITER + this.getEmail() +
                CSV_DELIMITER + this.getLoginInfo().getPassword() +
                CSV_DELIMITER + this.getFirstName() +
                CSV_DELIMITER + this.getLastName();
    }

    /**
    * Sets the budget of the user.
    * 
    * @param newBudget the new budget of the user
    */
    public void setBudget(Budget newBudget) {
        this.budget = newBudget;
    }

    /**
    * Calculates and returns the total amount in all non-pension accounts of the user.
    * 
    * @return the total amount in all non-pension accounts of the user
    */
    public String amountAllAccounts() {

        return ApplicationObjects.numberRegex(this.accounts
                .stream()
                .filter(account -> !account.getAccountType().equals(AccountCategory.PENSION_ACCOUNT))
                .map(Account::getAmount)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add)
                .toString());
    }

    /**
    * Calculates and returns the total expenses of the user in the last year.
    * 
    * @return the total expenses of the user in the last year
    */
    public String expensesLastYear() {
        return incomeOrExpensesLastYear(false);
    }

    /**
    * Calculates and returns the total income of the user in the last year.
    * 
    * @return the total income of the user in the last year
    */
    public String incomeLastYear() {
        return incomeOrExpensesLastYear(true);
    }

    /**
    * Calculates and returns the total expenses of the user in all accounts in the last 30 days.
    * 
    * @return the total expenses of the user in all accounts in the last 30 days
    */
    public String expensesLast30Days() {
        return incomeOrExpensesAllAccountsLast30Days(false);
    }

    /**
    * Calculates and returns the total income of the user in all accounts in the last 30 days.
    * 
    * @return the total income of the user in all accounts in the last 30 days
    */
    public String incomeLast30Days() {
        return incomeOrExpensesAllAccountsLast30Days(true);
    }

    /**
    * Checks if an account with the specified name exists in the user's accounts.
    * 
    * @param accountName the name of the account to check
    * @return true if an account with the specified name exists, false otherwise
    */
    public boolean checkIfAccountNameIsOccupied(String accountName) {
        return this.accounts.stream()
                .anyMatch(account -> account.getAccountName()
                        .equalsIgnoreCase(accountName));
    }

    /**
    * Checks if an account with the specified number exists in the user's accounts.
    * 
    * @param accountNumber the number of the account to check
    * @return true if an account with the specified number exists, false otherwise
    */
    public boolean checkIfAccountNumberIsOccupied(String accountNumber) {
        return  this.accounts.stream()
                .anyMatch(account -> account.getAccountNumber()
                        .equalsIgnoreCase(accountNumber));
    }

    /**
    * Gets the account with the specified name from the user's accounts.
    * 
    * @param accountName the name of the account to get
    * @return the account with the specified name
    * @throws NoSuchElementException if no account with the specified name exists
    */
    public Account getAccountWithAccountName(String accountName) {
        return this.accounts.stream()
                .filter(account -> account.getAccountName()
                        .equals(accountName))
                .findFirst()
                .orElseThrow();
    }

    /**
    * Returns the account object with the specified account number.
    *
    * @param accountNumber the account number to search for
    * @return the account object with the specified account number
    * @throws java.util.NoSuchElementException if no account with the specified account number is found
    */
    public Account getAccountWithAccountNumber(String accountNumber) {
        return this.accounts.stream()
                .filter(account -> account.getAccountNumber()
                        .equals(accountNumber))
                .findFirst()
                .orElseThrow();
    }

    /**
    * Removes the specified account from the list of accounts.
    *
    * @param account the account to remove
    */
    public void removeAccount(Account account){
        this.accounts.remove(account);
    }

    /**
    * Adds the specified transaction to the list of transactions.
    *
    * @param transaction the transaction to add
    */
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    /**
    * Returns the total income or expenses of all accounts in the last 30 days, depending on the value of the parameter.
    *
    * @param incomeOrExpenses true to return total income, false to return total expenses
    * @return a string representation of the total income or expenses
    */
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

    /**
    * Returns the total income or expenses of all accounts in the last year, depending on the value of the parameter.
    *
    * @param incomeOrExpenses true to return total income, false to return total expenses
    * @return a string representation of the total income or expenses
    */
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

    /**
    * Returns the total expenses of the specified budget category in the last 30 days.
    *
    * @param category the budget category to get the total expenses for
    * @return the total expenses of the specified budget category in the last 30 days
    */
    public double getTotalExpenseOfCategoryLast30Days(String category) {
        return getTotalExpenseOrIncomeOfCategoryLast30Days(category);
    }

    /**
    * Returns the total income of the specified budget category in the last 30 days.
    *
    * @param category the budget category to get the total income for
    * @return the total income of the specified budget category in the last 30 days
    */
    public double getTotalIncomeOfCategoryLast30Days(String category) {
        return getTotalExpenseOrIncomeOfCategoryLast30Days(category);
    }

    /**
    * Returns the total expenses of the specified budget category in the last 30 days.
    *
    * @param category the budget category to get the total expenses for
    * @return the total expenses of the specified budget category in the last 30 days
    */
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

    /**
    * Returns the total expenses of the specified budget category in the last year.
    *
    * @param category the budget category to get the total expenses for
    * @return the total expenses of the specified budget category in the last year
    */
    public double getTotalExpenseOfCategoryLastYear(String category) {
        return getTotalExpenseOrIncomeOfCategoryLastYear(category, true);
    }

    /**
    * Returns the total income of the specified budget category in the last year.
    *
    * @param category the budget category to get the total income for
    * @return the total income of the specified budget category in the last year
    */
    public double getTotalIncomeOfCategoryLastYear(String category) {
        return getTotalExpenseOrIncomeOfCategoryLastYear(category, false);
    }

    /**
    * Calculates the total expenses or income for a specific budget category for the last calendar year.
    * 
    * @param category the name of the budget category to calculate expenses or income for
    * @param expenseOrIncome a boolean indicating whether to calculate expenses (true) or income (false)
    * @return the total amount of expenses or income for the given category over the last calendar year
    */
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
