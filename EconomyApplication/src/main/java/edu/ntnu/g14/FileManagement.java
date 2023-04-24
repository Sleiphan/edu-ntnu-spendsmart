package edu.ntnu.g14;


import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

import edu.ntnu.g14.dao.*;


//TODO: create exceptions

public class FileManagement {
    public static final String PATH_ACCOUNTS     = "saves/accounts.txt";
    public static final String PATH_BUDGETS      = "saves/budgets.txt";
    public static final String PATH_INVOICES     = "saves/invoices.txt";
    public static final String PATH_TRANSACTIONS = "saves/transactions.txt";
    public static final String PATH_USERS        = "saves/users.txt";

    private static final Charset DATA_CHARSET = StandardCharsets.UTF_8;
    private static final TransactionDAO TRANSACTION_DAO;
    private static final AccountDAO ACCOUNT_DAO;
    private static final InvoiceDAO INVOICE_DAO;
    private static final BudgetDAO BUDGET_DAO;
    private static final UserDAO USER_DAO;

    private static final String[] PATH_ALL = { PATH_ACCOUNTS, PATH_BUDGETS, PATH_INVOICES, PATH_TRANSACTIONS, PATH_USERS };

    static {
        try {
            establishSaveFiles();

            // Initialise data access objects
            TRANSACTION_DAO = new TransactionDAO(PATH_TRANSACTIONS, DATA_CHARSET);
            ACCOUNT_DAO     = new AccountDAO    (PATH_ACCOUNTS,     DATA_CHARSET);
            INVOICE_DAO     = new InvoiceDAO    (PATH_INVOICES,     DATA_CHARSET);
            BUDGET_DAO      = new BudgetDAO     (PATH_BUDGETS,      DATA_CHARSET);
            USER_DAO        = new UserDAO       (PATH_USERS,        DATA_CHARSET);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void establishSaveFiles() throws IOException {
        for (String s : PATH_ALL) {
            Path file = Path.of(s);
            int lastFS = s.lastIndexOf('/');
            if (lastFS == -1)
                lastFS = s.lastIndexOf('\\');
            if (lastFS != -1) {
                Files.createDirectories(Path.of(s.substring(0, lastFS)));
            }

            try {
                Files.createFile(file);
            } catch (FileAlreadyExistsException e) {
                // Skip
            }
        }
    }

    public static Login[] readUsers() throws IOException {
        return USER_DAO.getAllLogins();
    }

    public static User readUser(String userId) throws IOException {
        User user = USER_DAO.getUser(userId);

        Transaction[] transactions = TRANSACTION_DAO.getAllTransactionsForUser(userId);
        Account[] accounts = ACCOUNT_DAO.getAllAccountsForUser(userId);
        Invoice[] invoices = INVOICE_DAO.getAllInvoices(userId);
        Budget budget = BUDGET_DAO.getBudget(userId);

        return new User(accounts, invoices, user.getLoginInfo(), user.getEmail(), user.getLastName(), user.getFirstName(), transactions, budget);
    }

    public static void writeNewUser(User newUser) throws IOException {
        String userID = newUser.getLoginInfo().getUserId();

        if (USER_DAO.getUser(userID) != null)
            throw new IllegalArgumentException("A user with that user ID already exists");

        USER_DAO.setUser(newUser);

        Budget budget = newUser.getBudget();
        Invoice[] invoices = newUser.getAllInvoices().toArray(Invoice[]::new);
        Transaction[] transactions = newUser.getTransactions();
        Account[] accounts = newUser.getAccounts();


        if (budget != null)
            BUDGET_DAO.setBudget(userID, budget);

        if (invoices != null)
            for (Invoice invoice : invoices)
                INVOICE_DAO.addNewInvoice(userID, invoice);

        if (transactions != null)
            for (Transaction transaction : transactions)
                TRANSACTION_DAO.addNewTransaction(userID, transaction);

        if (accounts != null)
            ACCOUNT_DAO.replaceAllAccounts(userID, accounts);
    }

    public static void writeAccount(String userId, Account account) {
        try {
            ACCOUNT_DAO.setAccount(userId, account);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeTransaction(String userId, Transaction transaction) {
        try {
            TRANSACTION_DAO.addNewTransaction(userId, transaction);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void editAccount(String userID, Account account) {
        try {
            ACCOUNT_DAO.setAccount(userID, account);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void newEditUser(User loggedInUser) {
        try {
            USER_DAO.setUser(loggedInUser);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Transaction[] readLatestTransactions(String userId, int amount) throws IOException{
        return TRANSACTION_DAO.getLatest(userId, amount);
    }

}
