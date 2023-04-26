package edu.ntnu.g14;


import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import edu.ntnu.g14.dao.*;

/**
 * The class is responsible for handling file I/O for the application.
 * It provides methods for reading and writing data to text files.
 * The class ensures the necessary save files are created if they do not exist.
 */
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

    /**
    * Creates the save files for the application if they don't exist.
    * @throws IOException if an error occurs while creating the directories or files.
    */
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
                if (Files.notExists(file))
                    Files.createFile(file);
            } catch (FileAlreadyExistsException e) {
                // Skip
            }
        }
    }

    /**
    * Returns an array of all Login objects for all users in the application.
    * @return an array of Login objects.
    * @throws IOException if an error occurs while reading from the data file.
    */
    public static Login[] readLogins() throws IOException {
        return USER_DAO.getAllLogins();
    }

    /**
    * Returns a User object with the specified user ID.
    * @param userId the ID of the user to retrieve.
    * @return a User object with the specified user ID.
    * @throws IOException if an error occurs while reading from the data file.
    */
    public static User readUser(String userId) throws IOException {
        User user = USER_DAO.getUser(userId);

        Transaction[] transactions = TRANSACTION_DAO.getAllTransactionsForUser(userId);
        Account[] accounts = ACCOUNT_DAO.getAllAccountsForUser(userId);
        Invoice[] invoices = INVOICE_DAO.getAllInvoices(userId);
        Budget budget = BUDGET_DAO.getBudget(userId);

        return new User(accounts, invoices, user.getLoginInfo(), user.getEmail(), user.getLastName(), user.getFirstName(), transactions, budget);
    }

    /**
    * Writes changes to a User object to the data file.
    * @param loggedInUser the updated User object to write to the data file.
    * @throws RuntimeException if an IOException occurs while writing to the data file.
    */
    public static void newEditUser(User loggedInUser) {
        try {
            USER_DAO.setUser(loggedInUser);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
    * Writes a new User object to the data file.
    * @param newUser the new User object to write to the data file.
    * @throws IllegalArgumentException if a user with the same ID already exists in the application.
    * @throws IOException if an error occurs while writing to the data file.
    */
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

    /**
    * Deletes the specified user and all associated data from the application.
    * @param userID the ID of the user to delete.
    * @throws RuntimeException if an IOException occurs while deleting the user's data from the data file.
    */
    public static void deleteUser(String userID) {
        try {
            TRANSACTION_DAO.deleteUser(userID);
            ACCOUNT_DAO.deleteUser(userID);
            INVOICE_DAO.deleteUser(userID);
            BUDGET_DAO.deleteUser(userID);
            USER_DAO.deleteUser(userID);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
    * Writes a new Transaction object to the data file for the specified user.
    * @param userId the ID of the user to associate the new transaction with.
    * @param transaction the new Transaction object to write to the data file.
    * @throws RuntimeException if an IOException occurs while writing to the data file.
    */
    public static void writeTransaction(String userId, Transaction transaction) {
        try {
            TRANSACTION_DAO.addNewTransaction(userId, transaction);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
    * Returns an array of the specified number of the most recent transactions for the specified user.
    * @param userId the ID of the user to retrieve transactions for.
    * @param amount the number of transactions to retrieve.
    * @return an array of the specified number of the most recent transactions for the specified user.
    * @throws IOException if an error occurs while reading from the data file.
    */
    public static Transaction[] readLatestTransactions(String userId, int amount) throws IOException{
        return TRANSACTION_DAO.getLatest(userId, amount);
    }

    /**
    * Writes a new or updated Account object to the data file for the specified user.
    * @param userId the ID of the user to associate the new or updated account with.
    * @param account the Account object to write to the data file.
    * @throws RuntimeException if an IOException occurs while writing to the data file.
    */
    public static void writeAccount(String userId, Account account) {
        try {
            ACCOUNT_DAO.setAccount(userId, account);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
    * Edits an existing Account object in the data file for the specified user.
    * @param userID the ID of the user to associate the edited account with.
    * @param account the updated Account object to write to the data file.
    * @throws RuntimeException if an IOException occurs while writing to the data file.
    */
    public static void editAccount(String userID, Account account) {
        try {
            ACCOUNT_DAO.setAccount(userID, account);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
