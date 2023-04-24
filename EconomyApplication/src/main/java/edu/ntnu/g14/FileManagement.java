package edu.ntnu.g14;


import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import edu.ntnu.g14.dao.*;


//TODO: create exceptions

public class FileManagement {
    public static final String PATH_ACCOUNTS     = "saves/accounts.txt";
    public static final String PATH_BUDGETS      = "saves/budgets.txt";
    public static final String PATH_INVOICES     = "saves/invoices.txt";
    private static final String PATH_TEMPFILE    = "saves/temp_file.txt";
    public static final String PATH_TRANSACTIONS = "saves/transactions.txt";
    public static final String PATH_USERS        = "saves/users.txt";

    private static final Charset DATA_CHARSET = StandardCharsets.UTF_8;
    private static final TransactionDAO TRANSACTION_DAO;
    private static final AccountDAO ACCOUNT_DAO;
    private static final InvoiceDAO INVOICE_DAO;
    private static final BudgetDAO BUDGET_DAO;

    private static final String[] PATH_ALL = { PATH_ACCOUNTS, PATH_BUDGETS, PATH_INVOICES, PATH_TRANSACTIONS, PATH_USERS };

    static {
        try {
            establishSaveFiles();

            // Initialise data access objects
            TRANSACTION_DAO = new TransactionDAO(PATH_TRANSACTIONS, DATA_CHARSET);
            ACCOUNT_DAO     = new AccountDAO    (PATH_ACCOUNTS,     DATA_CHARSET);
            INVOICE_DAO     = new InvoiceDAO    (PATH_INVOICES,     DATA_CHARSET);
            BUDGET_DAO      = new BudgetDAO     (PATH_BUDGETS,      DATA_CHARSET);
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
        
        List<String> lines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(PATH_USERS));
        int count = 0;
        //reader.readLine();
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
            if (!line.startsWith("  ")) {
                count++;
            } 
        }
        String[] linesArray = lines.toArray(new String[0]);
        
        

        Login[] logins = new Login[count];
        for(int i = 0; i < count; i++){
            String[] user = linesArray[i].split(",");
            logins[i] = new Login(user[1], user[3], user[0]);
        }
        reader.close();
        return logins;
    }

    public static Transaction[] readAllTransactions(String userID) throws IOException {
        return TRANSACTION_DAO.getAllTransactionsForUser(userID);
    }

    public static User readUser(String userId) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(PATH_USERS));
        Stream<String> userInfoLine = reader.lines()
                .filter(line -> line.startsWith(userId + ","));

        Transaction[] transactions = TRANSACTION_DAO.getAllTransactionsForUser(userId);
        Account[] accounts = ACCOUNT_DAO.getAllAccountsForUser(userId);
        Invoice[] invoices = INVOICE_DAO.getAllInvoices(userId);
        Budget budget = BUDGET_DAO.getBudget(userId);

        String[] userInfo = userInfoLine.flatMap(s -> Stream.of(s.split(","))
                        .skip(1)
                        .map(String::toString))
                .toArray(String[]::new);

        String username = userInfo[0];
        String email = userInfo[1];
        String password = userInfo[2];
        String firstName = userInfo[3];
        String lastName = userInfo[4];

        Login loginInfo = new Login(username,password, userId);

        return new User(accounts, invoices, loginInfo, email, lastName, firstName, transactions, budget);
        
    }

    public static void writeNewUser(User newUser) throws IOException{
        newEditUser(newUser);
        setAllAccounts(newUser);


        String addonTextTransactions = newUser.getLoginInfo().getUserId() + ",";
        try (RandomAccessFile file = new RandomAccessFile(PATH_TRANSACTIONS, "rw")) {
            String line;
            while ((line = file.readLine()) != null) {
                if (line.startsWith(" ")) {
                    file.seek(file.getFilePointer() - line.length() - 2);
                    file.write(addonTextTransactions.getBytes());
                    file.close();
                    break;
                }
            }
        }
    }

    public static Invoice[] getInvoicesForUser(String userID) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(PATH_INVOICES));
        Stream<String> userTrans = reader.lines()
                .filter(line -> line.startsWith(userID + ","));
        System.out.println(userTrans);

        String[] userTransArray = userTrans.toArray(String[]::new);
        List<Invoice> transactionList = new ArrayList<>();
        for (String s : userTransArray) {
            String[] placeHolder = s.split(",");
            String[] transactionArray = new String[placeHolder.length - 1];
            for(int i =  0; i < placeHolder.length - 1; i++){
                transactionArray[i] = placeHolder[i];
            }

            for (int i = 1; i < transactionArray.length; i++) {
                Invoice transaction = Invoice.fromCSVString(transactionArray[i]);
                transactionList.add(transaction);
            }
        }
        Invoice[] invoices = transactionList.toArray(Invoice[]::new);




        reader.close();
        return invoices;
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

    public static void setAllAccounts(User loggedInUser) {
        try {
            ACCOUNT_DAO.replaceAllAccounts(loggedInUser.getLoginInfo().getUserId(), loggedInUser.getAccounts());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void newEditUser(User loggedInUser) {

        String userId = loggedInUser.getLoginInfo().getUserId();

        String newLine = loggedInUser.toCSVString();
        editAccountOrUser(newLine, userId, PATH_USERS);

    }
    private static void editAccountOrUser(String newLine, String userId, String filePath) {
        final String LINE_DELIMITER = "\n";
        final Path accountOrUserFilePath = Path.of(filePath);
        final Path directory = Path.of(filePath.substring(0, filePath.lastIndexOf("/")));
        String content = "";

        if (newLine.startsWith(LINE_DELIMITER))
            newLine = newLine.substring(1);
        if (newLine.endsWith(LINE_DELIMITER))
            newLine = newLine.substring(0, newLine.length() - 1);

        try {
            content = Files.readString(accountOrUserFilePath);
        } catch (FileNotFoundException e) {
            try {
                Files.createDirectories(directory);
                Files.createFile(accountOrUserFilePath);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        boolean entryFound = false;

        String[] lines = content.split(LINE_DELIMITER);
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].startsWith(userId)) {
                lines[i] = newLine;
                entryFound = true;
                break;
            }
        }

        StringBuilder sb = new StringBuilder();
        if (entryFound) {
            for (String line : lines)
                sb.append(line).append(LINE_DELIMITER);
        } else {
            sb.append(newLine);
        }

        try {
            Files.writeString(accountOrUserFilePath, sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeNewInvoice(String userId, Invoice invoice) throws IOException{
        String addonText = "" + invoice.getDueDate() + "." + invoice.getAmount() + "." + invoice.getRecipientAccountNumber() + ",";
        try (RandomAccessFile file = new RandomAccessFile(PATH_INVOICES, "rw")) {
            String line;
            long pos = 0;
            while ((line = file.readLine()) != null) {
                if (line.startsWith(userId + ",")) {
                    pos = file.getFilePointer() - line.length() + line.indexOf("   ") - 1;
                    file.seek(pos);
                    file.write(addonText.getBytes());
                    file.close();
                    break;
                }
            }
        }
    }

    public static void writeNewBudget(String userId, Budget budget) throws IOException {
        String addonText = ""; //TODO: make in format of budget
        try (RandomAccessFile file = new RandomAccessFile(PATH_BUDGETS, "rw")) {
            String line;
            long pos = 0;
            while ((line = file.readLine()) != null) {
                if (line.startsWith(userId + ",")) {
                    pos = file.getFilePointer() - line.length() + line.indexOf("   ") - 1;
                    file.seek(pos);
                    file.write(addonText.getBytes());
                    file.close();
                    break;
                }
            }
        }
    }

    public static Transaction[] readLatestTransactions(String userId, int amount) throws IOException{
        return TRANSACTION_DAO.getLatest(userId, amount);
    }

}
